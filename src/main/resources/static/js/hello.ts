interface User {
  name: string;
  id: number;
}
const user: User = {
  name: "Joe",
  id: 1,
};

let message: string = `Hello Vite World! User: ${user.name}`;
console.log(message);