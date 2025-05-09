<template>
  <TopBar :title="title"/>
  <div class="search">
    <el-form :inline="true" :model="search_form" class="search_form">
      <!--学员名称-->
      <el-form-item label="学员名称">
        <el-input
            v-model="search_form.name"
            placeholder="请输入学员名称"
            clearable
            class="search_input"
            style="width: 200px"
        />
      </el-form-item>
      <!--学号-->
      <el-form-item label="学号">
        <el-input
            v-model="search_form.num"
            placeholder="请输入学号"
            clearable
            class="search_input"
            style="width: 200px"
        />
      </el-form-item>
      <!--最高学历-->
      <el-form-item label="最高学历">
        <el-select
            v-model="search_form.degree"
            placeholder="请选择"
            size="large"
            style="width: 200px"
        >
          <el-option
              v-for="item in degrees"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          />
        </el-select>
      </el-form-item>

      <!--所属班级-->
      <el-form-item label="所属班级">
        <el-select
            v-model="search_form.className"
            placeholder="请选择"
            size="large"
            style="width: 200px"
        >
          <el-option
              v-for="item in classData"
              :key="item.id"
              :label="item.className"
              :value="item.className"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button class="search_btn" type="primary" @click="load">查询</el-button>
        <el-button class="search_btn" type="success" @click="reset">重置</el-button>
      </el-form-item>
    </el-form>
  </div>

  <div class="btn">
    <div class="add_btn">
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        新增学员
      </el-button>
    </div>

    <div class="delete_btn">
      <el-button type="primary" @click="deleteMore">
        <el-icon><Minus /></el-icon>
        批量删除
      </el-button>
    </div>
  </div>

  <div class="table">
    <el-table :data="data.tableData" border @selection-change="handleSectionChange" :header-cell-style="{ fontSize: '18px' }">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column prop="name" label="姓名" width="85" align="center" />
      <el-table-column prop="num" label="学号" width="150" align="center" />
      <el-table-column prop="className" label="班级" width="150" align="center" />
      <el-table-column prop="gender" label="性别" width="80" align="center" :formatter="formatGender"/>
      <el-table-column prop="phone" label="手机号" width="150" align="center" />
      <el-table-column prop="degree" label="最高学历" width="100" align="center" :formatter="formatDegree"/>
      <el-table-column prop="count" label="违纪次数" width="100" align="center" />
      <el-table-column prop="point" label="违纪扣分" width="100" align="center" />
      <el-table-column prop="updateTime" label="最后操作时间" width="200" align="center" />
      <el-table-column label="操作" min-width="150" align="center">
        <template #default="scope">
          <el-button link type="primary" size="small" @click="handleEdit(scope.row)" style="font-size: 16px">编辑</el-button>
          <el-button link type="primary" size="small" @click="handlePoint(scope.row)" style="font-size: 16px; color: orange">违纪</el-button>
          <el-button link type="primary" size="small" @click="handleDelete(scope.row)" style="font-size: 16px; color: red">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>

  <div class="pagination">
    <el-pagination
        @size-change="load"
        @current-change="load"
        v-model:current-page="data.pageNum"
        v-model:page-size="data.pageSize"
        :page-sizes="[5, 10, 15, 20]"
        background
        layout="sizes, total, prev, pager, next, jumper"
        :total="data.total"
    />
  </div>

  <!-- 新增学员弹窗 -->
  <el-dialog
      v-model="data.addStudentVisible"
      width="500"
      align-center
  >
    <TopBar title="新增学员"></TopBar>
    <el-form ref="addRef" :model="data.form" :rules="data.rules" class="form_container" label-width="80px">
      <el-form-item label="姓名" prop="name">
        <el-input v-model="data.form.name" placeholder="请输入姓名" autocomplete="off"/>
      </el-form-item>
      <el-form-item label="学号" prop="num">
        <el-input v-model="data.form.num" placeholder="请输入学号" autocomplete="off"/>
      </el-form-item>
      <el-form-item label="性别">
        <el-select v-model="data.form.gender" placeholder="请选择" autocomplete="off">
          <el-option label="男" value="1"></el-option>
          <el-option label="女" value="2"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="手机号" prop="phone">
        <el-input v-model="data.form.phone" placeholder="请输入手机号" autocomplete="off"/>
      </el-form-item>
      <el-form-item label="最高学历">
        <el-select v-model="data.form.degree" placeholder="请选择" autocomplete="off">
          <el-option label="初中" value="1"></el-option>
          <el-option label="高中" value="2"></el-option>
          <el-option label="大专" value="3"></el-option>
          <el-option label="本科" value="4"></el-option>
          <el-option label="硕士" value="5"></el-option>
          <el-option label="博士" value="6"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="所属班级">
        <el-select v-model="data.form.className" placeholder="请选择" autocomplete="off">
          <el-option
              v-for="item in classData"
              :key="item.id"
              :label="item.className"
              :value="item.className"
          >
          </el-option>
        </el-select>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button  type="primary" @click="addSave">保存</el-button>
        <el-button @click="addCancel">取消</el-button>
      </div>
    </template>
  </el-dialog>

  <!-- 编辑学员弹窗 -->
  <el-dialog
      v-model="data.editStudentVisible"
      width="500"
      align-center
  >
    <TopBar title="编辑学员"></TopBar>
    <el-form ref="editRef" :model="data.form" :rules="data.rules" class="form_container" label-width="80px">
      <el-form-item label="姓名" prop="name">
        <el-input v-model="data.form.name" placeholder="请输入姓名" autocomplete="off"/>
      </el-form-item>
      <el-form-item label="学号" prop="num">
        <el-input v-model="data.form.num" placeholder="请输入学号" autocomplete="off"/>
      </el-form-item>
      <el-form-item label="性别">
        <el-select v-model="data.form.gender" placeholder="请选择" autocomplete="off">
          <el-option label="男" value="男"></el-option>
          <el-option label="女" value="女"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="手机号" prop="phone">
        <el-input v-model="data.form.phone" placeholder="请输入手机号" autocomplete="off"/>
      </el-form-item>
      <el-form-item label="最高学历">
        <el-select v-model="data.form.degree" placeholder="请选择" autocomplete="off">
          <el-option label="初中" value="1"></el-option>
          <el-option label="高中" value="2"></el-option>
          <el-option label="大专" value="3"></el-option>
          <el-option label="本科" value="4"></el-option>
          <el-option label="硕士" value="5"></el-option>
          <el-option label="博士" value="6"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="所属班级">
        <el-select v-model="data.form.className" placeholder="请选择" autocomplete="off">
          <el-option
              v-for="item in classData"
              :key="item.id"
              :label="item.className"
              :value="item.className"
          >
          </el-option>
        </el-select>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button  type="primary" @click="editSave">保存</el-button>
        <el-button @click="data.editStudentVisible = false">取消</el-button>
      </div>
    </template>
  </el-dialog>

  <!-- 违纪弹窗 -->
  <el-dialog
      v-model="data.pointVisible"
      width="500"
      align-center
  >
    <TopBar title="学员违纪处理"></TopBar>
    <el-form class="form_container" label-width="80px">
      <el-form-item label="违纪扣分">
        <el-input v-model="data.form.point" autocomplete="off"/>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button  type="primary" @click="handlePointSave">确定</el-button>
        <el-button @click="handlePointCancel">取消</el-button>
      </div>
    </template>
  </el-dialog>

  <!-- 删除学员弹窗 -->
  <el-dialog
      v-model="data.deleteStudentVisible"
      width="500"
      align-center
  >
    <TopBar title="删除学员"></TopBar>
    <template #footer>
      <div class="dialog-footer">
        <el-button  type="primary" @click="handleDelete">确定</el-button>
        <el-button @click="data.deleteStudentVisible = false">取消</el-button>
      </div>
    </template>
  </el-dialog>

</template>

<script setup>
import TopBar from "@/components/topBar/TopBar.vue";
import {ref, reactive} from "vue";
import {Plus, Minus} from "@element-plus/icons-vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";

const title = ref("学员管理")

const search_form = reactive({
  name: "",
  num: "",
  degree: "",
  className: ""
})

const degrees = [
  {
    value: '1',
    label: '初中'
  },
  {
    value: '2',
    label: '高中'
  },
  {
    value: '3',
    label: '大专'
  },
  {
    value: '4',
    label: '本科'
  },
  {
    value: '5',
    label: '硕士'
  },
  {
    value: '6',
    label: '博士'
  },
]

const search = () => {
  console.log(search_form)
}

const data = reactive({
  tableData: [],
  pageNum: 1,
  pageSize: 10,
  total: 0,
  addStudentVisible: false,
  editStudentVisible: false,
  deleteStudentVisible: false,
  deleteMoreStudentVisible: false, // 批量删除
  pointVisible: false,

  form:{
    id: "",
    name: "",
    num: "",
    gender: "",
    phone: "",
    degree: "",
    className: "",
    point: "", // 违纪分值
    currentPoint: "", // 当前违纪分值
  },
  rules: {
    name: [
      { required: true, message: '请输入姓名', trigger: 'blur' },
      { min: 2, max: 10, message: '班级名称非法,长度在10到10个字符', trigger: 'blur' }
    ],
    num: [
      { required: true, message: '请输入学号', trigger: 'blur' },
      { min: 10, max: 10, message: '10位学号', trigger:'blur' }
    ],
    phone: [
      { required: true, message: '请输入手机号', trigger: 'blur' },
      { min: 11, max: 11, message: '11位手机号', trigger:'blur' }
    ],
  },
  deleteMoreIds: []
})

const classData = reactive([])
const getClass = () => {
  request.get('class/selectAll').then(res => {
    console.log("班级", res)
    classData.push(...res.data)
    console.log("班级信息", classData)
  })
}
getClass()

const load = () => {
  request.get('student/selectPage',{
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name: search_form.name,
      num: search_form.num,
      degree: search_form.degree,
      className: search_form.className
    }
  }).then(res => {
    console.log(res.data)
    data.tableData = res.data.list
    data.total = res.data.total
  })
}

load()

const reset = () => {
  console.log('重置')
  search_form.name = null
  search_form.num = null
  search_form.className = null
  search_form.degree = null
  load()
}

const addRef = ref(null)
const editRef = ref(null)

const handleAdd = () => {
  data.addStudentVisible = true
  data.form = {}
  if(addRef.value){
    addRef.value.resetFields()
  }
}

// 添加，保存
const addSave = () => {
  console.log('添加保存')
  console.log(data.form)
  request.post('student/add', data.form).then(res => {
    console.log(res)
    if (res.code === '200') {
      ElMessage.success(res.msg)
      load()
    } else {
      ElMessage.error('新增学员失败')
    }
    load()
    data.addStudentVisible = false
  })
}

// 添加，取消
const addCancel = () => {
  console.log('添加取消')
  Object.assign(data.form, {
    name: "",
    num: "",
    gender: "",
    phone: "",
    degree: "",
    className: ""
  })
  data.addStudentVisible = false
}


// 编辑，打开编辑弹窗
const handleEdit = (row) => {
  console.log('编辑的行数据:', row) 
  if (editRef.value) {
    editRef.value.resetFields()
  }
  data.form = JSON.parse(JSON.stringify(row))
  console.log(data.form)
  data.editStudentVisible = true
  if(editRef.value){
    editRef.value.resetFields()
  }
}

// 编辑，保存
const editSave = () => {
  // console.log('编辑保存')
  // console.log(data.form)
  const payload = {
    ...data.form,
    gender: data.form.gender === "男" ? 1 : 2, 
  }
  request.put('student/update', payload).then(res => {
    // console.log(res.data)
    load()
    if(res.code === '200'){
      ElMessage.success(res.msg)

    }else{
      ElMessage.error('编辑失败')
    }
  })
  data.editStudentVisible = false
}

// 违纪，弹出违纪弹窗
const handlePoint = (row) => {
  // console.log('违纪行的id是：', row.id)
  // console.log('当前扣分：', row.point)
  data.form.id = row.id
  data.form.currentPoint = row.point

  data.pointVisible = true
}

// 违纪，保存
const handlePointSave = () => {
  // console.log('违纪保存')
  // console.log('违纪行的id是：', data.form.id)
  // console.log('当前扣分：', data.form.currentPoint)
  // console.log('违纪分值：', data.form.point)

  request.put(`student/point/${data.form.id}`, data.form.point).then(res => {
    // console.log(res.data)
    load()
    if(res.code === '200'){
      ElMessage.success(res.msg)
      load()
    }else{
      ElMessage.error('扣分失败')
    }
  })
  data.pointVisible = false
}

// 违纪，取消
const handlePointCancel = () => {
  // console.log('违纪保存')
  // console.log(data.point)
  data.pointVisible = false
  data.point = ""
}

// 删除，弹出删除弹窗
const handleDelete = (row) => {
  // console.log('删除行的id是：', row.id)
  Object.assign(data.form, row)
  ElMessageBox.confirm('确定删除该学员吗？删除将不可撤销', '删除学员', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    // console.log('删除保存')
    // console.log(data.form)
    request.delete(`student/delete/${data.form.id}`).then(res => {
      console.log(res.data)
      load()
      if(res.code === '200'){
        ElMessage.success(res.msg)
        load()
      }else{
        ElMessage.error('删除失败')
      }
    })
  }).catch(() => {
    // console.log('删除取消')
    data.deleteStudentVisible = false
  })
}

const handleSectionChange = (rows) => {
  // console.log(rows)
  // 从选中的行数组中提取行id组成新数组
  data.deleteMoreIds = rows.map(row => row.id)
  // console.log(data.deleteMoreIds)
}

// 批量删除
const deleteMore = () => {
  if(data.deleteMoreIds.length === 0){
    ElMessage.warning('请选择要删除的数据')
    return
  }
  // console.log('批量删除')
  // console.log(data.deleteMoreIds)
  ElMessageBox.confirm('确定删除选中的学员吗？删除将不可撤销', '批量删除', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    request.delete('student/deleteMore', {
      data: data.deleteMoreIds
    }).then(res => {
      console.log(res.data)
      load()
      if(res.code === '200'){
        ElMessage.success(res.msg)
        load()
      }else{
        ElMessage.error('批量删除失败')
      }
    })
  }).catch(() => {
    console.log('批量删除取消')
  })
}

// 性别匹配
const formatGender = (row, column, cellValue) => {
  const genderMap = { 1: '男', 2: '女' }
  return genderMap[cellValue] || '未知'
}

// 职位匹配
const formatDegree = (row, column, cellValue) => {
  const degreeMap = {
    1: '初中',
    2: '高中',
    3: '大专',
    4: '本科',
    5: '硕士',
    6: '博士'
  }
  return degreeMap[cellValue] || '未知'
}

</script>

<style scoped>
.search{
  width: 1440px;
  margin-top: 2rem;
  padding-left: 1.5rem;

  .search_form{
    display: flex;
    justify-content: space-between;
    align-items: center;

    .search_input{
      width: 300px;
      height: 45px;
    }

    .search_btn{
      width: 100px;
      height: 45px;
    }
  }
}

.btn{
  display: flex;
  justify-content: space-between;
  width: 230px;
  margin-top: 2rem;
  margin-left: 2rem;
}

.table{
  width: 1331px;
  margin-top: 2rem;
  margin-left: 2rem;
}

.form_container{
  margin-top: 1.5rem;
  margin-left: 2rem;
  margin-right: 2rem;
}

.pagination{
  display: flex;
  justify-content: space-between;
  width: 1190px;
  margin-top: 2rem;
  margin-left: 2rem;
}

.delete_content{
  margin: 1rem 0;
  p{
    font-size: 16px;
    letter-spacing: 1px;
    text-align: center;
  }
}

.el-form-item {
  display: flex;
  align-items: center;
}

</style>
