var path = require('path');

module.exports = {
    // publicPath: process.env.NODE_ENV === 'production' ? (process.env.CONTEXT_PATH || '/') : '/',
    configureWebpack: {
        devtool: 'source-map',
        resolve: {
            modules: [path.resolve('src'), 'node_modules']
        }
    },
    // proxy all webpack dev-server requests starting with /api
    // to our Spring Boot backend (localhost:8098) using http-proxy-middleware
    // see https://cli.vuejs.org/config/#devserver-proxy
    devServer: {
        proxy: {
            [(process.env.CONTEXT_PATH || '/')]: {
                target: 'http://localhost:8080', // this configuration needs to correspond to the Spring Boot backends' application.properties server.port
                ws: true,
                changeOrigin: true
            }
        }
    },
    // Change build paths to make them Maven compatible
    // see https://cli.vuejs.org/config/
    outputDir: 'target/dist',
    assetsDir: 'static'
}
