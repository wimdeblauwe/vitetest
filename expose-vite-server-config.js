import fs from 'fs';
import path from 'path';

export default function writeServerConfigPlugin() {
  const configDir = path.resolve(process.cwd(), 'target', 'thvite');
  const configPath = path.join(configDir, 'server-config.json');

  return {
    name: 'write-server-config',
    configureServer(server){
      server.httpServer?.once('listening',()=>{

        try {
          ensureDirectoryExistence(configPath)
          // Read the existing config file
          const configData = JSON.parse(fs.readFileSync(configPath, 'utf8'));

          // Update the port in the config
          configData.port = server.config.server.port;

          // Write the updated config back to the file
          fs.writeFileSync(configPath, JSON.stringify(configData, null, 2));
        } catch (error) {
          console.error('Error updating server-config.json:', error);
        }
      })
    },
    configResolved(config) {
      const host = config.server.host || 'localhost';
      const port = config.server.port || 5173;
      const configData = JSON.stringify({ host, port }, null, 2);

      ensureDirectoryExistence(configPath);
      fs.writeFileSync(
          path.resolve(process.cwd(), configPath),
          configData
      );
    },
  };
}


function ensureDirectoryExistence(filePath) {
  const dirname = path.dirname(filePath);
  if (fs.existsSync(dirname)) {
    return true;
  }
  ensureDirectoryExistence(dirname);
  fs.mkdirSync(dirname);
}