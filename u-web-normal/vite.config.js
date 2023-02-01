import { defineConfig } from 'vite';
import uni from "@dcloudio/vite-plugin-uni";
import {config} from './src/config.js';
import {msgPlugin} from '@wzabcd/u-ui';
import  {resolve} from  'path';
export default defineConfig({
  base: '/',
  server:{
    port: config.port,
    proxy: {
          '/api/': {
            target: config.url,
            changeOrigin: true,
            rewrite: (path) => path.replace(/^\/api/, '/'),
          },
        },
  },
  resolve:{
    alias:[
      {
        find:"@",
        replacement:resolve(__dirname,'src')
      },
      {
        find:"&",
        replacement:resolve(__dirname,'/')
      }
    ]
  },
  build:{
    // sourcemap: true,
  },
  plugins: [
    msgPlugin(true),
    uni(),

  ]
})
