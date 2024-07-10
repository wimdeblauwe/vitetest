import fs from 'fs';
import path from 'path';
import {globby } from 'globby';

function ensureDirectoryExistence(filePath) {
  const dirname = path.dirname(filePath);
  if (fs.existsSync(dirname)) {
    return true;
  }
  ensureDirectoryExistence(dirname);
  fs.mkdirSync(dirname);
}

function copyFile(src, dest) {
  ensureDirectoryExistence(dest);
  fs.copyFileSync(src, dest);
  console.log(`Copied ${src} to ${dest}`);
}

export default function copyHtmlTemplatesPlugin() {
  const outputDir = path.resolve(process.cwd(), 'target', 'classes');

  return {
    name: 'copy-html-templates',
    async configureServer(server) {
      const rootDir = server.config.root;

      // Copy all HTML files when the server starts
      const files = await globby('**/*.html', {cwd: rootDir});
      files.forEach(file => {
        const srcPath = path.join(rootDir, file);
        const destPath = path.join(outputDir, file);
        copyFile(srcPath, destPath);
      });
    },
    handleHotUpdate({ file, server }) {
      if (path.extname(file) === '.html') {
        const relativePath = path.relative(server.config.root, file);
        const outputPath = path.join(outputDir, relativePath);
        copyFile(file, outputPath);
      }
    }
  };
}