module.exports = {
    presets: [
        '@vue/cli-plugin-babel/preset'
    ],
    plugins: [
        ["import",
            {
                libraryName: "ant-design-vue", //按需引入Antd
                libraryDirectory: "es",
                style: true,
            }
        ]
    ]
}
