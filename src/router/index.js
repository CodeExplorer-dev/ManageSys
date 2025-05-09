import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/LoginView.vue'),
  },
  {
    path: '/',
    redirect: "/login"
  },
  {
    path: '/manageSys',
    component: () => import('../layout/MainLayout.vue'),
    redirect: 'department',
    children:[
      {
        path: 'class',
        name: 'ClassManage',
        component: () => import('../views/ClassView.vue')
      },
      {
        path: 'student',
        name: 'StudentManage',
        component: () => import('../views/StudentView.vue')
      },
      {
        path: 'department',
        name: 'DepartmentManage',
        component: () => import('../views/DepartmentView.vue')
      },
      {
        path: 'employee',
        name: 'EmployeeManage',
        component: () => import('../views/EmployeeView.vue')
      },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('../views/ProfileView.vue')
      }
    ]
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem("token");
  // 如果没有 token 且目标页面不是登录页面，则重定向到登录
  if (!token) {
    if (to.path !== "/login") {
      console.info("无token，直接跳转到登录界面");
      return next("/login");
    }
  }
  next(); // 默认继续导航
});

export default router
