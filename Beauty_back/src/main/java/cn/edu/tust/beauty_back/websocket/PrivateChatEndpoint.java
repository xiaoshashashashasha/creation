package cn.edu.tust.beauty_back.websocket;

import cn.edu.tust.beauty_back.bean.PrMessage;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/ws/privateChat")
@Component
public class PrivateChatEndpoint {

    private static final ConcurrentHashMap<String, Session> sessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        // 从查询参数中获取 userId
        String userId = extractUserIdFromQuery(session.getQueryString());
        if (userId == null || userId.trim().isEmpty()) {
            System.out.println("无法获取 userId，拒绝 WebSocket 连接");
            try {
                session.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, "缺少 userId 参数"));
            } catch (IOException e) {
                System.out.println("关闭 WebSocket 连接失败: " + e.getMessage());
                e.printStackTrace();
            }
            return;
        }

        sessions.put(userId, session);

        try {
            // 发送 JSON 格式的连接成功消息
            String connectMsg = "{\"type\": \"connection\", \"status\": \"success\", \"message\": \"连接成功\"}";
            session.getBasicRemote().sendText(connectMsg);
            System.out.println("用户ID"+userId+"连接成功");
        } catch (IOException e) {
            System.out.println("向用户 " + userId + " 发送连接成功消息失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        String userId = extractUserIdFromQuery(session.getQueryString());
        if (userId != null) {
            sessions.remove(userId);
        } else {
            System.out.println("无法获取 userId，关闭未知用户的 WebSocket 连接，原因: " + reason);
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        String userId = extractUserIdFromQuery(session.getQueryString());
        try {
            // 回复 JSON 格式的消息
            String replyMsg = "{\"type\": \"reply\", \"message\": \"收到消息: " + message + "\"}";
            session.getBasicRemote().sendText(replyMsg);
        } catch (IOException e) {
            System.out.println("向用户 " + (userId != null ? userId : "未知用户") + " 回复消息失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        String userId = extractUserIdFromQuery(session.getQueryString());
        System.out.println("用户 " + (userId != null ? userId : "未知用户") + " 的 WebSocket 发生错误: " + error.getMessage());
        error.printStackTrace();
    }

    /**
     * 向指定用户发送消息
     *
     * @param userId  目标用户ID
     * @param message 消息内容对象
     */
    public static void sendMessage(Integer userId, PrMessage message) {
        if (userId == null) {
            System.out.println("目标用户ID为空，无法发送消息");
            return;
        }
        String userIdStr = userId.toString();
        Session session = sessions.get(userIdStr);
        if (session != null && session.isOpen()) {
            try {
                // 格式化消息内容为 JSON
                String msgContent = formatMessage(message);
                session.getBasicRemote().sendText(msgContent);
            } catch (IOException e) {
                System.out.println("通过 WebSocket 向用户 " + userIdStr + " 发送消息失败: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("用户 " + userIdStr + " 未在线或会话已关闭，无法发送消息");
        }
    }

    /**
     * 格式化消息内容为 JSON 字符串
     *
     * @param message 消息对象
     * @return 格式化后的消息字符串
     */
    private static String formatMessage(PrMessage message) {
        // 转换为 JSON 格式
        String timestamp = "N/A";
        if (message.getCreated_at() != null) {
            timestamp = message.getCreated_at().toString(); // LocalDateTime 默认格式为 ISO 8601
        }
        StringBuilder formattedMsg = new StringBuilder();
        formattedMsg.append("{")
                .append("\"type\": \"message\", ")
                .append("\"from_id\": ").append(message.getFrom_id()).append(", ")
                .append("\"content\": \"").append(message.getContent()).append("\", ")
                .append("\"created_at\": \"").append(timestamp).append("\", ")
                .append("\"type\": ").append(message.getType()).append(", ")
                .append("\"content_id\": ").append(message.getType()).append(", ")
                .append("\"cover_pic\": \"").append(message.getCover_pic()).append("\", ")
                .append("\"title\": \"").append(message.getTitle()).append("\"")
                .append("}");
        return formattedMsg.toString();
    }

    /**
     * 从查询字符串中提取 userId
     *
     * @param queryString 查询字符串，例如 "userId=8"
     * @return 提取的 userId 值，如果未找到则返回 null
     */
    private String extractUserIdFromQuery(String queryString) {
        if (queryString == null || queryString.isEmpty()) {
            return null;
        }
        String[] params = queryString.split("&");
        for (String param : params) {
            if (param.startsWith("userId=")) {
                return param.substring("userId=".length());
            }
        }
        return null;
    }
}
