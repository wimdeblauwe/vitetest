import { defineConfig } from "vite";
import path, { resolve } from "path";
import writeServerConfigPlugin from "./expose-vite-server-config.js";
import copyHtmlTemplatesPlugin from "./copy-html-templates.js";
// import { viteStaticCopy } from 'vite-plugin-static-copy'

export default defineConfig({
  plugins: [writeServerConfigPlugin(), copyHtmlTemplatesPlugin()],
  // viteStaticCopy({
  //   targets: [
  //     {
  //       src: path.resolve(__dirname, 'src/main/resources/templates'),
  //       dest: '.'//path.resolve(__dirname, 'target/classes/templates')
  //     }
  //   ]
  // })],
  root: path.join(__dirname, "./src/main/resources"),
  publicDir: "templates",
  // assetsInclude: ['**/*.html'],
  // resolve: {
  //   alias: {
  //     "@": resolve(__dirname, "/src"),
  //   },
  // },
  build: {
    manifest: true,
    rollupOptions: {
      input: "/static/css/application.css",/*{
        main: "./src/main/resources/static/css/application.css",
      },*/
      // output: {
      //   entryFileNames: `[name].js`,
      //   chunkFileNames: `[name].js`,
      //   assetFileNames: `[name].[ext]`
      // }
/*
      output: {
        assetFileNames: assetInfo => {
          const info = assetInfo.name.split('.');
          const extType = info[info.length - 1];
          console.log(extType);
          return `[name]-[hash].${extType}`;
        }
      }
*/
    },
    outDir: path.join(__dirname,`./target/classes/static`),
    copyPublicDir: false,
    emptyOutDir: true
  },
  // server: {
  //   port: 8081,
  //   host: "0.0.0.0",
  //   // origin: 'http://localhost:8081', // generated asset URLs will be resolved using the back-end server URL instead of a relative path
  // }
});

/*function CustomHmr() {
  return {
    name: 'custom-hmr',
    enforce: 'post',
    // HMR
    handleHotUpdate({ file, server }) {
      if (file.endsWith('.html')) {
        console.log('reloading html file...', file );

        server.ws.send({
          type: 'full-reload',
          path: '*'
        });
      }
    },
  }
}*/

