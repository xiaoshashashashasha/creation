const { defineConfig } = require('@vue/cli-service')

module.exports = defineConfig({
  transpileDependencies: true,

  devServer: {
    port: 8888,      // 设置你想要的端口
    open: true,      // 启动自动打开浏览器
    proxy: {
      '/api': {
        target: 'http://localhost:8080', // 后端地址
        changeOrigin: true, //修改源
        pathRewrite: { '^/api': '' }     // 将 /api 替换为空字符串
      }
    }
  }
})
