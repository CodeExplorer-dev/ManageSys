<template>
  <!-- TODO:输入框校验规则 -->
  <TopBar :title="title"/>
  <div class="search">
    <el-form :inline="true" :model="search_form" class="search_form">
      <el-form-item label="班级名称">
        <el-input
            v-model="search_form.className"
            placeholder="请输入班级名称"
            clearable
            class="search_input"
        />
      </el-form-item>
      <el-form-item label="结课时间">
        <el-date-picker
            v-model="search_form.date"
            type="datetimerange"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
            date-format="YYYY/MM/DD"
            class="search_time"
        />
      </el-form-item>
      <el-form-item>
        <!--   TODO:这里查询只完成了班级名称查询，还没有完成日期查询     已完成-->
        <el-button class="search_btn" type="primary" @click="load">查询</el-button>
        <el-button class="search_btn" type="success" @click="reset">重置</el-button>
      </el-form-item>
    </el-form>
  </div>

  <div class="add_btn">
    <el-button type="primary" @click="handleAdd">
      <el-icon><Plus /></el-icon>
      新增班级
    </el-button>
  </div>

  <div class="table">
    <el-table :data="data.tableData" border>
      <el-table-column label="序号" width="150" align="center">
        <template #default="{ $index }">
          {{ (data.pageNum - 1) * data.pageSize + $index + 1 }}
        </template>
      </el-table-column>
      <el-table-column prop="className" label="班级名称" width="150" align="center" />
      <el-table-column prop="classRoom" label="班级教室" width="120" align="center" />
      <el-table-column prop="start" label="开课时间" width="200" align="center" />
      <el-table-column prop="end" label="结课时间" width="200" align="center" />
      <el-table-column prop="teacher" label="班主任" width="120" align="center" />
      <el-table-column label="操作" min-width="120" align="center">
        <template #default="scope">
          <el-button link type="primary" size="small" @click="handleEdit(scope.row)">
            编辑
          </el-button>
          <el-button link type="primary" size="small" @click="handleDelete(scope.row)">删除</el-button>
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

  <!--  新增班级弹窗  -->
  <el-dialog
      v-model="data.addClassVisible"
      width="500"
      align-center
  >
    <TopBar title="新增班级"></TopBar>
    <el-form ref="addRef" :model="data.form" class="form_container" :rules="data.rules" label-width="80px" >
      <el-form-item label="班级名称" prop="className">
        <el-input v-model="data.form.className" placeholder="请输入班级名称，如：2024第01期10班" autocomplete="off"/>
      </el-form-item>
      <el-form-item label="班级教室" prop="classRoom">
        <el-input v-model="data.form.classRoom" placeholder="请填写班级教室" autocomplete="off"/>
      </el-form-item>
      <el-form-item label="开课时间">
        <el-date-picker
            v-model="data.form.start"
            type="date"
            placeholder="请选择开课时间"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
            autocomplete="off"
        />
      </el-form-item>
      <el-form-item label="结课时间">
        <el-date-picker
            v-model="data.form.end"
            type="date"
            placeholder="请选择结课时间"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
            autocomplete="off"
        />
      </el-form-item>
      <el-form-item label="班主任">
        <el-select v-model="data.form.teacher" placeholder="请选择" autocomplete="off">
          <el-option
              v-for="item in teacherData"
              :key="item.id"
              :label="item.name"
              :value="item.name"
          >
          </el-option>
        </el-select>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button  type="primary"  @click="save">保存</el-button>
        <el-button @click="cancel">取消</el-button>
      </div>
    </template>
  </el-dialog>

  <!--  编辑班级弹窗  -->
  <el-dialog
      v-model="data.editClassVisible"
      width="500"
      align-center
  >
    <TopBar title="编辑班级"></TopBar>
    <el-form ref="editRef" :model="data.form" class="form_container" :rules="data.rules" label-width="80px">
      <el-form-item label="班级名称" prop="className">
        <el-input v-model="data.form.className" placeholder="请输入班级名称，如：2024第01期10班" />
      </el-form-item>
      <el-form-item label="班级教室">
        <el-input v-model="data.form.classRoom" placeholder="请填写班级教室" />
      </el-form-item>
      <el-form-item label="开课时间">
        <el-date-picker
            v-model="data.form.start"
            type="date"
            placeholder="请选择开课时间"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
        />
      </el-form-item>
      <el-form-item label="结课时间">
        <el-date-picker
            v-model="data.form.end"
            type="date"
            placeholder="请选择结课时间"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
        />
      </el-form-item>
      <el-form-item label="班主任">
        <el-select v-model="data.form.teacher" placeholder="请选择" autocomplete="off">
          <el-option
              v-for="item in teacherData"
              :key="item.id"
              :label="item.name"
              :value="item.name"
          >
          </el-option>
        </el-select>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button  type="primary"  @click="saveEdit">保存</el-button>
        <el-button @click="cancelEdit">取消</el-button>
      </div>
    </template>
  </el-dialog>

  <!--  删除班级弹窗  -->
  <el-dialog
      v-model="data.deleteClassVisible"
      width="500"
      align-center
  >
    <TopBar title="删除班级"></TopBar>
    <div class="delete_content">
      <p>确定删除该班级吗？删除将不可撤销</p>
    </div>
    <template #footer>
      <div class="dialog-footer">
        <el-button  type="primary"  @click="confirmDelete">确定</el-button>
        <el-button @click="cancelDelete">取消</el-button>
      </div>
    </template>
  </el-dialog>

</template>

<script setup>
import TopBar from "@/components/topBar/TopBar.vue";
import {ref, reactive} from "vue";
import request from "@/utils/request.js";
import {Plus} from "@element-plus/icons-vue";
import {ElMessage} from "element-plus";
import {tableData} from "@/mock/class.js";

const title = ref("班级管理")

const search_form = reactive({
  className: '',
  date: []
})

const search = () => {
  console.log(search_form)
}

const data = reactive({
  tableData: [],
  pageNum: 1,
  pageSize: 5,
  total: 0,
  addClassVisible: false,
  editClassVisible: false,
  deleteClassVisible: false,
  form: {
    id: 0,
    className: "",
    classRoom: "",
    start: "",
    end: "",
    teacher: ""
  },
  // 新增表单验证
  rules: {
    className: [
      {required: true, message: '请输入班级名称', trigger: 'blur'},
      {min: 4, max: 30, message: '班级名称非法,长度在4到30个字符', trigger: 'blur'}
    ],
    classRoom: [
      {required: true, message: '请输入班级教室', trigger: 'blur'},
      {min: 1, max: 20, message: '长度在1到20个字符', trigger: 'blur'}
    ]
  }
})

const teacherData = reactive([])
const getTeacher = () => {
  request.get('employee/selectAll').then(res => {
    // console.log("班主任", res)
    teacherData.push(...res.data)
    // console.log("班主任", teacherData)
  })
}
getTeacher()

const load = () => {
  request.get('class/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      className: search_form.className,
      start: search_form.date[0],
      end: search_form.date[1]
    }
  }).then(res => {
    console.log(res.data)
    data.tableData = res.data.list
    data.total = res.data.total
  })
}

load()

const reset = () => {
  search_form.className = null
  search_form.date = []
  load()
}

const addRef = ref(null)
const editRef = ref(null)

const handleAdd = () => {
  data.addClassVisible = true
  if (addRef.value) {
    addRef.value.resetFields();
  }
}

// 添加保存
const save = () => {
  console.log(data.form)
  const payload = {
    className: data.form.className,
    classRoom: data.form.classRoom,
    start: data.form.start,
    end: data.form.end,
    teacher: data.form.teacher
  }
  request.post('class/add', payload).then(res => {
    // console.log(res.data)
    load()
    if(res.code === '200'){
      ElMessage.success(res.msg)
    }else{
      ElMessage.error('新增班级失败')
    }
  })
  load()
  data.addClassVisible = false
  //清空addData内容
  // for (let key in addData) {
  //   addData[key] = ''
  // }

}

// 添加取消
const cancel = () => {
  data.addClassVisible = false
  for (let key in data.form) {
    data.form[key] = ''
  }
}

// 编辑,打开编辑弹窗
const handleEdit = (row) => {
  console.log('编辑行的id是：', row.id)
  data.form = JSON.parse(JSON.stringify(row))
  // Object.assign(addData, row)
  data.editClassVisible = true
}

// 编辑保存
const saveEdit = () => {
  console.log('根据id更新:', data.form.id)
  const payload = {
    id: data.form.id,
    className: data.form.className,
    classRoom: data.form.classRoom,
    start: data.form.start,
    end: data.form.end,
    teacher: data.form.teacher
  }
  request.put('class/update', payload).then(res => {
    // console.log(res.data)
    load()
    if(res.code === '200'){
      ElMessage.success(res.msg)
    }else{
      ElMessage.error('编辑失败')
    }
  })
  data.editClassVisible = false
  //清空addData内容
  for (let key in data.form) {
    data.form[key] = ''
  }
}

// 编辑取消
const cancelEdit = () => {
  data.editClassVisible = false
  for (let key in data.form) {
    data.form[key] = ''
  }
}

// 删除，打开删除弹窗
const handleDelete = (row) => {
  // console.log('删除行的id是：', row.id)
  data.form = JSON.parse(JSON.stringify(row))
  data.deleteClassVisible = true
}

// 删除确定
const confirmDelete = () => {
  // console.log('delete id:', addData.id)
  request.delete(`class/delete/${data.form.id}`).then(res => {
    // console.log(res.data)
    load()
    if(res.code === '200'){
      ElMessage.success(res.msg)
    }else{
      ElMessage.error('删除失败')
    }
  })
  data.deleteClassVisible = false
}

// 删除取消
const cancelDelete = () => {
  data.deleteClassVisible = false
}
</script>

<style scoped>
.search{
  width: 1415px;
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

.add_btn{
  margin-top: 2rem;
  margin-left: 2rem;
}

.form_container{
  margin-top: 1.5rem;
  margin-left: 2rem;
}

.table{
  width: 1060px;
  margin-top: 2rem;
  margin-left: 2rem;
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
.el-table__header-wrapper{
  background-color: skyblue;
}

</style>

