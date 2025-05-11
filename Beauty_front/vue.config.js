const { defineConfig } = require('@vue/cli-service');

module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    port: 8888,
    open: true,
    proxy: {
      '/api': {
        target: 'http://localhost:8080', // 后端地址
        changeOrigin: true, // 修改源
      },
      '/ws/privateChat': {
        target: 'http://localhost:8080', // 后端地址
        changeOrigin: true, // 修改源
        ws: true // 启用 WebSocket 代理
      }
    }
  }
});
