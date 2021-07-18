var path = require('path');

const CONTEXT_PATH = process.env.VUE_APP_CONTEXT_PATH || '/';
const API_PATH = process.env.VUE_APP_API_PATH || '/';

const DEV_PORT = 8091;
const DEV_BACKEND_URL = 'http://localhost:8080';

const assetsDir = 'static';

module.exports = {
    publicPath: CONTEXT_PATH,
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
        port: DEV_PORT,
        proxy: {
            [API_PATH]: {
                target: DEV_BACKEND_URL, // this configuration needs to correspond to the Spring Boot backends' application.properties server.port
                ws: true,
                changeOrigin: true
            }
        }
    },
    // Change build paths to make them Maven compatible
    // see https://cli.vuejs.org/config/
    outputDir: 'target/dist',
    assetsDir: assetsDir
}
