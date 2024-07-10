import fs from 'fs';
import path from 'path';
import {globby} from 'globby';

function ensureDirectoryExistence(filePath) {
  const dirname = path.dirname(filePath);
  if (fs.existsSync(dirname)) {
    return true;
  }
  ensureDirectoryExistence(dirname);
  fs.mkdirSync(dirname);
}

function copyFile(src, dest, logCopy = false) {
  ensureDirectoryExistence(dest);
  fs.copyFileSync(src, dest);

  if (logCopy) {
    console.log(`Copied ${src} to ${dest}`);
  }
}

async function copyHtmlFiles(rootDir, outputDir) {
  // Copy all HTML files when the server starts
  const files = await globby('**/*.html', {cwd: rootDir});
  files.forEach(file => {
    const srcPath = path.join(rootDir, file);
    const destPath = path.join(outputDir, file);
    copyFile(srcPath, destPath);
  });
  console.log(`Copied ${files.length} files to ${outputDir}`);
}

export default function copyHtmlTemplatesPlugin() {
  const outputDir = path.resolve(process.cwd(), 'target', 'classes');
  let config;
  return {
    name: 'copy-html-templates',
    configResolved(resolvedConfig) {
      config = resolvedConfig;
    },
    async configureServer(server) {
      const rootDir = server.config.root;
      await copyHtmlFiles(rootDir, outputDir);
    },
    handleHotUpdate({file, server}) {
      if (path.extname(file) === '.html') {
        const relativePath = path.relative(server.config.root, file);
        const outputPath = path.join(outputDir, relativePath);
        copyFile(file, outputPath, true);
      }
    },
    async buildEnd() {
      const rootDir = config.root;
      copyHtmlFiles(rootDir, outputDir);
    }
  };
}