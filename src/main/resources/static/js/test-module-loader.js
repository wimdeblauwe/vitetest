document.getElementById('load-module1').addEventListener('click', async () => {
  const module = await import('./module1.js')
  document.getElementById('output').textContent = module.message1
})

document.getElementById('load-module2').addEventListener('click', async () => {
  const module = await import('./module2.js')
  document.getElementById('output').textContent = module.message2
})