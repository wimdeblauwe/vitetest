import {setupCounter} from "./counter.js";
import { createApp } from 'vue'
// import './style.css'
import HelloWorld from "../../components/HelloWorld.vue";

// createApp(HelloWorld).mount('#hello-world-vue')


const app = createApp({});

app.component('hello-world', HelloWorld);

app.mount("#app");

setupCounter(document.querySelector('#counter'));