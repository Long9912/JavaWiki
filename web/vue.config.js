const Timestamp = new Date().getTime()
module.exports = {
    productionSourceMap: false,
    pwa: {
        iconPaths: {
            favicon32: 'favicon.ico',
            favicon16: 'favicon.ico',
            appleTouchIcon: 'favicon.ico',
            maskIcon: 'favicon.ico',
            msTileImage: 'favicon.ico'
        }
    },
    css: {
        loaderOptions: {
            //主题样式，修改此文件后需要重新启动，
            less: {
                lessOptions: {
                    modifyVars: {
                        //这是配置css主题色
                        'primary-color': '#007AFF',
                    },
                    javascriptEnabled: true,
                },
            },
        },
        // 每次打包后生成的css携带时间戳
        extract: {
            filename: `css/[name].${Timestamp}.css`,
            chunkFilename: `css/[name].${Timestamp}.css`,
        },
    },
    configureWebpack: {
        //每次打包后生成的js携带时间戳
        output: {
            filename: `[name].${Timestamp}.js`,
            chunkFilename: `[name].${Timestamp}.js`,
        }
    }
}
