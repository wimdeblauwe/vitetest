import {defineConfig} from "vite";
import path from "path";
import writeServerConfigPlugin from "./expose-vite-server-config.js";
import copyHtmlTemplatesPlugin from "./copy-html-templates.js";

export default defineConfig({
  plugins: [writeServerConfigPlugin(), copyHtmlTemplatesPlugin()],
  root: path.join(__dirname, "./src/main/resources"),
  build: {
    manifest: true,
    rollupOptions: {
      input: [
        "/static/css/application.css",
        "/static/css/test.scss",
        "/static/js/index-page.js",
        "/static/js/hello.ts"],
    },
    outDir: path.join(__dirname, `./target/classes/static`),
    copyPublicDir: false,
    emptyOutDir: true
  }
});