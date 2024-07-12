import {defineConfig} from "vite";
import path from "path";
import writeServerConfigPlugin from "./expose-vite-server-config.js";
import copyHtmlTemplatesPlugin from "./copy-html-templates.js";
import vue from '@vitejs/plugin-vue';

export default defineConfig({
  plugins: [writeServerConfigPlugin(), copyHtmlTemplatesPlugin(), vue()],
  root: path.join(__dirname, "./src/main/resources"),
  build: {
    manifest: true,
    rollupOptions: {
      input: [
        "/static/css/application.css",
        "/static/css/test.scss",
        "/static/js/index-page.js",
        "/static/js/hello.ts",
        "/static/js/test-module-loader.js",
        "/components/HelloWorld.vue",
      ],
    },
    outDir: path.join(__dirname, `./target/classes/static`),
    copyPublicDir: false,
    emptyOutDir: true
  },
  resolve: {
    alias: {
      vue: "vue/dist/vue.esm-bundler.js",
    },
  },
});