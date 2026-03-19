<template>
  <div>
    <el-row>
      <el-col :span="3"><el-tag type="success">今日已检查{{ finish_check_count }}人</el-tag></el-col>
      <el-col :span="3"><el-tag type="warning">当前有{{ wait_check_count }}人在排队</el-tag></el-col>
      <el-col :span="18"> </el-col>
    </el-row>
    <el-divider></el-divider>
    <div style="font-size:20px;text-align:left">
      <i class="el-icon-document-checked">检查申请</i>
    </div>
    <el-divider></el-divider>
    <el-row :gutter="20">
      <el-col :span="6"><el-input v-model="input_patient_id" placeholder="请输入患者病历号"></el-input></el-col>
      <el-col :span="6"><el-input v-model="input_patient_name" placeholder="请输入患者姓名"></el-input></el-col>      
      <el-col :span="2"><el-button @click="getWaitCheck(1)">搜索</el-button></el-col>
      <el-col :span="10"> </el-col>
    </el-row>
    <el-divider></el-divider>
    <el-table
      :data="wait_check"      style="width: 80%">
      <el-table-column        type="index"     label="编号"        width="80"></el-table-column>
      <el-table-column        prop="real_name"        label="患者姓名"        width="180"></el-table-column>
      <el-table-column        prop="case_number"        label="患者病历号">     </el-table-column>
      <el-table-column label="操作">
      <template #default="scope">
        <el-button   size="mini" @click="createCheckApply(scope.$index, scope.row)">进行检查</el-button>
        <el-button size="mini" >跳过</el-button>
        <el-button size="mini" >叫号</el-button>
      </template>
    </el-table-column>
    </el-table>
    <el-divider></el-divider>
    <el-pagination
    :page-size="pageSize" :total="totalCount" @current-change="wait_patient_table_change"
    layout="prev, pager, next">
  </el-pagination>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import request from '@/util/util' //封装axios

// ---- 数据 ----
const input_patient_id = ref('')
const input_patient_name = ref('')
const pageSize = ref(5) // 每页显示行数
const totalCount = ref(0)
const finish_check_count = ref(0) // 已经检查完成人数
const wait_check_count = ref(0) // 等待检查人数
const wait_check = ref([])

// ---- 方法 ----

// 创建检查
function createCheckApply(scope_index) {
  const patient = {
    real_name: wait_check.value[scope_index].real_name,
    case_number: wait_check.value[scope_index].case_number,
    id: wait_check.value[scope_index].id,
    gender: wait_check.value[scope_index].gender,
    age: wait_check.value[scope_index].age
  }
  sessionStorage.setItem('check_now_patient', JSON.stringify(patient))
  // 提示选择
  window.$message?.('选择了患者：' + patient.real_name)
 
}

// 等待患者 table 数据改变时触发
function wait_patient_table_change(nowPageNumber) {
  getWaitCheck(nowPageNumber)
  console.log('等待患者table数据改变时触发的事件:', nowPageNumber)
}

// 得到已经检查完成人数
async function getFinishCheckCount() {
  try {
    const resp = await request.get('/getFinishCheckCount')
    finish_check_count.value = resp.data
  } catch (error) {
    console.error(error)
  }
}

// 得到等待检查人数
async function getWaitCheckCount() {
  try {
    const resp = await request.get('/getWaitCheckCount')
    wait_check_count.value = resp.data
  } catch (error) {
    console.error(error)
  }
}

// 得到等待检查患者信息
async function getWaitCheck(nowPageNumber = 1) {
  try {
    const resp = await request.get('/getWaitCheck', {
      params: {
        case_number: input_patient_id.value,
        real_name: input_patient_name.value,
        nowPageNumber,
        pageSize: pageSize.value
      }
    })
    wait_check.value = resp.data.list
    totalCount.value = resp.data.totalCount
  } catch (error) {
    console.error(error)
  }
}

// ---- 生命周期 ----
onMounted(() => {
  getFinishCheckCount()
  getWaitCheckCount()
  getWaitCheck(1)
})
</script>
