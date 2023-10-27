export default function authHeader() {
  const token = JSON.parse(sessionStorage.getItem("token"));

  if (token) {
    const element = {
      Authorization: `Bearer ${token.token}`,
    };
    return element;
  } else return {};
}
