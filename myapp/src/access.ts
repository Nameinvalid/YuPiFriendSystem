/**
 * @see https://umijs.org/zh-CN/plugins/plugin-access
 * 做全局的信息管理，在全局变量中拿到我们的个人信息
 * */
export default function access(initialState: { currentUser?: API.CurrentUser } | undefined) {
  const { currentUser } = initialState ?? {};
  return {
    canAdmin: currentUser && currentUser.userRole === 1,
  };
}
