<template>
  <TopBar :title="title"/>
  <div class="search">
    <el-form :inline="true" :model="search_form" class="search_form">
      <el-form-item label="姓名:">
        <el-input
            v-model="search_form.name"
            placeholder="请输入员工名称"
            clearable
            class="search_input"
            style="width: 200px"
        />
      </el-form-item>
      <el-form-item label="性别:">
        <el-select
            v-model="search_form.gender"
            placeholder="请选择"
            style="width: 200px"
        >
          <el-option label="男" value="1"></el-option>
          <el-option label="女" value="2"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="入职时间 从">
        <el-date-picker
            v-model="search_form.date"
            type="datetimerange"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
            format="YYYY-MM-DD"
            date-format="YYYY/MM/DD"
            value-format="YYYY-MM-DD"
        />
      </el-form-item>
      <el-form-item>
        <el-button class="search_btn" type="primary" @click="load">查询</el-button>
        <el-button class="search_btn" type="success" @click="reset">重置</el-button>
      </el-form-item>
    </el-form>
  </div>

  <div class="add_btn">
    <el-button type="primary" @click="handleAdd">
      <el-icon><Plus /></el-icon>
      新增员工
    </el-button>
  </div>

  <div class="table">
    <el-table :data="data.tableData" border :header-cell-style="{ fontSize: '18px' }">
      <el-table-column prop="name" label="姓名" width="150" align="center" />
      <el-table-column prop="image" label="图片" width="120" align="center" >
        <template #default="scope">
          <el-popover
              placement="top-start"
              trigger="hover"
          >
            <img :src="scope.row.image"  style="width: 125px; height: 120px" alt="">
            <template #reference>
              <img :src="scope.row.image" style="width: 50px; height: 50px" alt="">
            </template>
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column prop="gender" label="性别" width="100" align="center" :formatter="formatGender" />
      <el-table-column prop="job" label="职位" width="150" align="center" :formatter="formatJob"/>
      <el-table-column prop="deptId" label="所属部门" width="150" align="center" :formatter="formatDept"/>
      <el-table-column prop="entryDate" label="入职日期" width="200" align="center" />
      <el-table-column prop="updateTime" label="最后操作时间" width="200" align="center" />
      <el-table-column label="操作" min-width="120" align="center">
        <template #default="scope">
          <el-button link type="primary" size="small" @click="handleEdit(scope.row)" style="font-size: 16px">编辑</el-button>
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

  <!-- 新增员工弹窗 -->
  <el-dialog
      v-model="data.addEmpVisible"
      width="500"
      align-center
  >
    <TopBar title="新增员工"></TopBar>
    <el-form :model="data.form" class="form_container" label-width="80px">
      <el-form-item label="用户名">
        <el-input v-model="data.form.username" placeholder="请输入用户名，2-20字符，不可重复" autocomplete="off"/>
      </el-form-item>
      <el-form-item label="员工姓名">
        <el-input v-model="data.form.name" placeholder="请输入员工姓名，2-10个字" autocomplete="off"/>
      </el-form-item>
      <el-form-item label="性别">
        <el-select v-model="data.form.gender" placeholder="请选择" autocomplete="off">
          <el-option label="男" value="1"></el-option>
          <el-option label="女" value="2"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="图像">
        <el-upload
            class="avatar-uploader"
            action="http://localhost:8080/file/upload?module=image"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
        >
          <img v-if="data.form.image" :src="data.form.image" class="avatar"  alt=""/>
          <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
        </el-upload>
      </el-form-item>
      <el-form-item label="职位">
        <el-select v-model="data.form.job" placeholder="请选择" autocomplete="off">
          <el-option label="班主任" value="1"></el-option>
          <el-option label="讲师" value="2"></el-option>
          <el-option label="学工主管" value="3"></el-option>
          <el-option label="教研主管" value="4"></el-option>
          <el-option label="咨询师" value="5"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="入职日期">
        <el-date-picker
            v-model="data.form.entryDate"
            type="date"
            placeholder="请选择"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
            autocomplete="off"
        />
      </el-form-item>
      <el-form-item label="所属部门">
        <el-select v-model="data.form.deptId" placeholder="请选择" autocomplete="off">
          <el-option v-for="item in deptData" :key="item.id" :label="item.name" :value="item.id"></el-option>
        </el-select>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button  type="primary" @click="addSave">保存</el-button>
        <el-button @click="data.addEmpVisible = false">取消</el-button>
      </div>
    </template>
  </el-dialog>

  <!-- 编辑员工弹窗 -->
  <el-dialog
      v-model="data.editEmpVisible"
      width="500"
      align-center
  >
    <TopBar title="编辑员工"></TopBar>
    <el-form :model="data.form" class="form_container" label-width="80px">
      <el-form-item label="用户名">
        <el-input v-model="data.form.username" placeholder="请输入用户名，2-20字符，不可重复" autocomplete="off"/>
      </el-form-item>
      <el-form-item label="员工姓名">
        <el-input v-model="data.form.name" placeholder="请输入员工姓名，2-10个字" autocomplete="off"/>
      </el-form-item>
      <el-form-item label="性别">
        <el-select v-model="data.form.gender" placeholder="请选择" autocomplete="off">
          <el-option label="男" value="1"></el-option>
          <el-option label="女" value="2"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="图像">
        <el-upload
            class="avatar-uploader"
            action="http://localhost:8080/file/upload?module=image"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
        >
          <img v-if="data.form.image" :src="data.form.image" class="avatar"  alt=""/>
          <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
        </el-upload>
      </el-form-item>
      <el-form-item label="职位">
        <el-select v-model="data.form.job" placeholder="请选择" autocomplete="off">
          <el-option label="班主任" value="1"></el-option>
          <el-option label="讲师" value="2"></el-option>
          <el-option label="学工主管" value="3"></el-option>
          <el-option label="教研主管" value="4"></el-option>
          <el-option label="咨询师" value="5"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="入职日期">
        <el-date-picker
            v-model="data.form.entryDate"
            type="date"
            placeholder="请选择"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
            autocomplete="off"
        />
      </el-form-item>
      <el-form-item label="所属部门">
        <el-select v-model="data.form.deptId" placeholder="请选择" autocomplete="off">
          <el-option v-for="item in deptData" :key="item.id" :label="item.name" :value="item.id"></el-option>
        </el-select>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button  type="primary" @click="editSave">保存</el-button>
        <el-button @click="editCancel">取消</el-button>
      </div>
    </template>
  </el-dialog>

  <!-- 删除员工弹窗 -->
  <el-dialog
      v-model="data.deleteEmpVisible"
      width="500"
      align-center
  >
    <TopBar title="删除员工"></TopBar>
    <template #footer>
      <div class="dialog-footer">
        <el-button type="primary" @click="handleDelete">确定</el-button>
        <el-button @click="data.deleteEmpVisible = false">取消</el-button>
      </div>
    </template>
  </el-dialog>

</template>

<script setup>
import TopBar from "@/components/topBar/TopBar.vue";
import {ref, reactive} from "vue";
import request from "@/utils/request.js";
import {Plus} from "@element-plus/icons-vue";
import {ElMessage, ElMessageBox} from "element-plus";

const title = ref("员工管理")

const search_form = reactive({
  name: "",
  gender: "",
  date: [],
})

const search = () => {
  console.log(search_form.date[0])
  console.log(search_form.date[1])
}

const data = reactive({
  tableData: [],
  pageNum: 1,
  pageSize: 10,
  total: 0,
  addEmpVisible: false,
  editEmpVisible: false,
  deleteEmpVisible: false,
  form: {
    id: "",
    username: "",
    name: "",
    gender: "",
    image: null,
    job: "",
    entryDate: "",
    deptId: ""
  }
})

const deptData = reactive([])
const getDept = () => {
  request.get('depts/selectAll').then(res => {
    console.log("部门", res)
    deptData.push(...res.data)
    console.log("部门信息", deptData)
  })
}
getDept()

const load = () => {
  request.get('employee/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name: search_form.name,
      gender: search_form.gender,
      date1: search_form.date[0],
      date2: search_form.date[1]
    }
  }).then(res => {
    console.log(res.data)
    data.tableData = res.data.list
    data.total = res.data.total
  })
}

load()

const reset = () => {
  search_form.name = null
  search_form.gender = null
  search_form.date = []
  load()
}

const handleAvatarSuccess = (res, file) => {
  data.form.image = `http://localhost:8080/file/download?name=${res.data}`
}

const handleAdd = () => {
  data.addEmpVisible = true
  data.form = {}
}

const addSave = () => {
  if (!data.form.image) {
    ElMessage.error('请先选择头像图片');
    return;
  }
  console.log('添加保存')
  console.log(data.form)
  request.post('employee/add', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success(res.msg)
      load()
    } else {
      ElMessage.error('新增员工失败')
    }
  })
  data.addEmpVisible = false
}

// 编辑员工,打开弹窗
const handleEdit = (row) => {
  console.log('编辑行的id是：', row.id)
  data.form = JSON.parse(JSON.stringify(row))
  data.editEmpVisible = true
}

const editSave = () => {
  console.log(data.form)
  request.put('employee/update', data.form).then(res => {
    console.log(res)

    if(res.code === '200'){
      ElMessage.success(res.msg)
    }else{
      ElMessage.error('编辑员工失败')
    }
    load()
    Object.assign(data.form, {
      username: '',
      name: '',
      gender: '',
      image: '',
      job: '',
      entryDate: '',
      deptId: ''
    })
    data.editEmpVisible = false
  })
}

const editCancel = () => {
  data.editEmpVisible = false
}

// 删除，弹出删除弹窗
const handleDelete = (row) => {
  console.log('删除行的id是：', row.id)
  Object.assign(data.form, row)
  ElMessageBox.confirm('确定删除该员工吗？删除将不可撤销', '删除学员', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    // console.log('删除保存')
    // console.log(data.form)
    request.delete(`employee/delete/${data.form.id}`).then(res => {
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
    data.deleteEmpVisible = false
  })
}

// 性别匹配
const formatGender = (row, column, cellValue) => {
  const genderMap = { 1: '男', 2: '女' }
  return genderMap[cellValue] || '未知'
}

// 职位匹配
const formatJob = (row, column, cellValue) => {
  const jobMap = {
    1: '班主任',
    2: '讲师',
    3: '学工主管',
    4: '教研主管',
    5: '咨询师'
  }
  return jobMap[cellValue] || '未知'
}

const formatDept = (row, column, cellValue) => {
  const deptMap = {}
  deptData.forEach(dept => {
    deptMap[dept.id] = dept.name
  })
  return deptMap[cellValue] || '未知部门'
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
  margin-right: 2rem;
}

.table{
  width: 1190px;
  margin-top: 2rem;
  margin-left: 2rem;

}

.pagination{
  display: flex;
  justify-content: space-between;
  width: 1190px;
  margin-top: 2rem;
  margin-left: 2rem;
  margin-bottom: 2rem;
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

.avatar-uploader .avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>

<style>
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 130px;
  height: 130px;
  text-align: center;
}
</style>

