import axios from "axios";
import { ElMessage } from 'element-plus'

const request = axios.create({
    baseURL: 'http://localhost:8080',
    timeout: 30000
})

//request拦截器
//可以在请求前对请求做一些处理
request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=UTF-8'
    return config
}, error => {
    //对请求错误做些什么
    return Promise.reject(error)
})

//request拦截器
//在接口响应后统一处理结果
request.interceptors.response.use(
    response => {
        let res = response.data
        if(typeof res === 'string'){
            res = res ? JSON.parse(res) : res
        }
        return res;
    },
    //接口错误状态处理，也就是说无响应时的处理
    error => {
        if (error.response.status === 404) {
            ElMessage.error('未找到请求接口')
        } else if (error.response.status === 500) {
            ElMessage.error('系统异常，请检查后端控制台报错')
        } else {
            console.error(error.message)
        }
        return Promise.reject(error)
    }
)

export default request