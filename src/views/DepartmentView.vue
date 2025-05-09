<template>
  <TopBar :title="title"/>

  <div class="add_btn">
    <el-button type="primary" @click="data.addDeptVisible = true">
      <el-icon><Plus /></el-icon>
      新增部门
    </el-button>
  </div>

  <div class="table">
    <el-table :data="data.tableData" border>
      <el-table-column label="序号" width="150" align="center">
        <template #default="{ $index }">
          {{ (data.pageNum - 1) * data.pageSize + $index + 1 }}
        </template>
      </el-table-column>
      <el-table-column prop="name" label="部门名称" width="300" align="center"/>
      <el-table-column prop="updateTime" label="最后操作时间" width="300" align="center"/>
      <el-table-column label="操作" min-width="200" align="center">
        <template #default="scope">
          <el-button link type="primary" size="small" @click="handleEdit(scope.row)">
            编辑
          </el-button>
          <el-button link type="primary" size="small" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>

  <!-- 新增部门弹窗 -->
  <el-dialog
      v-model="data.addDeptVisible"
      width="500"
      align-center
  >
    <TopBar title="新增部门"></TopBar>
    <el-form-item label="部门名称" class="form_container">
      <el-input v-model="data.updateData.name" placeholder="请输入部门名称，长度为2-10位" />
    </el-form-item>
    <template #footer>
      <div class="dialog-footer">
        <el-button  type="primary"  @click="save">保存</el-button>
        <el-button @click="cancel">取消</el-button>
      </div>
    </template>
  </el-dialog>

  <!-- 编辑部门弹窗 -->
  <el-dialog
      v-model="data.editDeptVisible"
      width="500"
      align-center
      show-close="false"
  >
    <TopBar title="编辑部门"></TopBar>
    <el-form-item label="部门名称" class="form_container">
      <el-input v-model="data.updateData.name" placeholder="请输入部门名称，长度为2-10位" />
    </el-form-item>
    <template #footer>
      <div class="dialog-footer">
        <el-button  type="primary"  @click="saveEdit">保存</el-button>
        <el-button @click="cancelEdit">取消</el-button>
      </div>
    </template>
  </el-dialog>

  <!-- 删除部门弹窗 -->
  <el-dialog
      v-model="data.deleteDeptVisible"
      width="500"
      align-center
  >
    <TopBar title="删除部门"></TopBar>
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
import {reactive, ref} from "vue";
import {Plus} from "@element-plus/icons-vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";

const title = ref("部门管理")

// const addDeptVisible = ref(false)
// const editDeptVisible = ref(false)
// const deleteDeptVisible = ref(false)

const data = reactive({
  tableData: [],
  pageNum: 1,
  pageSize: 10,
  addDeptVisible: false,
  editDeptVisible: false,
  deleteDeptVisible: false,
  updateData: {
    id: '',
    name: ''
  }
})

const load = () => {
  request.get('depts/selectAll').then(res => {
    console.log(res.data)
    data.tableData = res.data
  })
}

load()

// TODO: 新增部门有问题 已解决
const save = async () => {
  console.log('add save')
  console.log(data.updateData.name)
  try {
    const department = { name: data.updateData.name };
    const res = await request.post('depts/add', department);
    console.log(res);
    if (res.code === '200') {
      ElMessage.success(res.msg);
      load();
      data.updateData.name = '';
      data.addDeptVisible = false;
    } else {
      ElMessage.error(`新增部门失败：${res.message || '未知错误'}`);
    }
  } catch (error) {
    console.error('请求失败：', error);
    ElMessage.error('请求失败，请稍后重试');
  }
}

const cancel = () => {
  console.log('add cancel')
  data.addDeptVisible = false
  data.updateData.name = ''
}

// 打开编辑弹窗
const handleEdit = (row) => {
  console.log('编辑行的id是：', row.id)
  data.updateData.name = row.name
  data.updateData.id = row.id
  data.editDeptVisible = true
}

const saveEdit = () => {
  console.log(data.updateData.name)
  request.put(`depts/update/${data.updateData.id}/${data.updateData.name}`).then(res => {
    console.log(res)
    load()
    if(res.code === '200'){
      ElMessage.success(res.msg)
    }else{
      ElMessage.error('编辑部门失败')
    }
  })
  data.updateData.name = ''
  data.editDeptVisible = false
}

const cancelEdit = () => {
  console.log('edit cancel')
  data.updateData.name = ''
  data.editDeptVisible = false
}

// 打开删除弹窗
const handleDelete = (row) => {
  console.log('删除行的id是：', row.id)
  Object.assign(data.updateData, row)
  data.deleteDeptVisible = true
}

const confirmDelete = () => {
  console.log('confirmDelete')
  request.delete(`depts/delete/${data.updateData.id}`).then(res => {
    console.log(res)
    load()
    if(res.code === '200'){
      ElMessage.success(res.msg)
    }else{
      ElMessage.error('删除部门失败')
    }
  })
  data.deleteDeptVisible = false
}

const cancelDelete = () => {
  console.log('cancelDelete')
  data.deleteDeptVisible = false
}
</script>

<style scoped>
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
  width: 1012px;
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
</style>
