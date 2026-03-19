<template>
  <div>
    <el-row>
      <el-col :span="3"><el-tag type="success">今日已看诊{{ finish_patient_count }}人</el-tag></el-col>
      <el-col :span="3"><el-tag type="warning">当前有{{ wait_patient_count }}人在排队</el-tag></el-col>
      <el-col :span="18"> </el-col>
    </el-row>
    <el-divider></el-divider>
    <div style="font-size:20px;text-align:left">
      <i class="el-icon-document-checked">患者叫号</i>
    </div>
    <el-divider></el-divider>
    <el-row :gutter="20">
      <el-col :span="6"><el-input v-model="input_patient_id" placeholder="请输入患者病历号"></el-input></el-col>
      <el-col :span="6"><el-input v-model="input_patient_name" placeholder="请输入患者姓名"></el-input></el-col>      
      <el-col :span="2"><el-button @click="getWaitPatient(1)">搜索</el-button></el-col>
      <el-col :span="10"> </el-col>
    </el-row>
    <el-divider></el-divider>
    <el-table
      :data="wait_patient"      style="width: 80%">
      <el-table-column        type="index"     label="编号"        width="80"></el-table-column>
      <el-table-column        prop="real_name"        label="患者姓名"        width="180"></el-table-column>
      <el-table-column        prop="case_number"        label="患者病历号">      </el-table-column>
      <!--
        <template slot-scope="scope">给这一列定义自定义内容
        scope是Element UI提供的一个对象，包含当前单元格的一些信息：
        scope.row：当前行的完整数据对象。
        scope.$index：当前行的索引
      -->
      <el-table-column label="操作">
      <template #default="scope">
        <el-button   size="mini" @click="createMedicalRecord(scope.$index, scope.row)">创建病历</el-button>
        <el-button size="mini" @click="showMsg()">跳过</el-button>
        <el-button size="mini" @click="showMsg()">叫号</el-button>
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

import { getSessionLoginUser}  from '../../util/util'
import request from "@/util/util"
import {ElMessage, ElMessageBox} from 'element-plus'

// --- 响应式数据 ---
const input_patient_id = ref('')
const input_patient_name = ref('')
const pageSize = ref(10)          // 每页显示行数
const totalCount = ref(0)
const finish_patient_count = ref(0) // 已经看诊完成人数
const wait_patient_count = ref(0)   // 等待看诊人数
const employee_id = ref(1)          // 当前医生ID
const wait_patient = ref([])

// --- 方法 ---
const createMedicalRecord = async (scope_index) => {
  const patient = {
    real_name: wait_patient.value[scope_index].real_name,
    case_number: wait_patient.value[scope_index].case_number,
    id: wait_patient.value[scope_index].id,
    gender: wait_patient.value[scope_index].gender,
    age: wait_patient.value[scope_index].age
  }
  sessionStorage.setItem("record_now_patient", JSON.stringify(patient))
  try {
    const resp = await request.get(`/treatPatient?id=${patient.id}`)
    ElMessage({
      message: `患者：${patient.real_name} ${resp.data.msg};请到 病例首页 开始诊治该患者`,
      type: 'success'
    }) 
    await getWaitPatient(1)
  } catch (error) {
    console.error(error)
    ElMessage({ message: '操作失败', type: 'error' })
  }
}

const getFinishPatientCount = async () => {
    // 模拟数据
    finish_patient_count.value = 10 
}

const getWaitPatientCount = async () => {
    // 模拟数据
    wait_patient_count.value = 2  
}

const getWaitPatient = async (nowPageNumber) => {
  try {
   
    const resp = await request.get(`/getWaitPatient`, {
      params: {
        employee_id: employee_id.value,
        case_number: input_patient_id.value,
        real_name: input_patient_name.value,
        nowPageNumber,
        pageSize: pageSize.value
      }
    })


    wait_patient.value = resp.data.list
    totalCount.value = resp.data.totalCount
  } catch (error) {
    console.error(error)
  }
}

const initLoginUserInfo = () => {
  const userInfo = getSessionLoginUser()
  employee_id.value = userInfo.id
}

const showMsg = () => {
  ElMessage({
    message: "需要 排号 子系统，连接失效",
    type: "error"
  })
}

// --- 生命周期 ---
onMounted(() => {
  initLoginUserInfo()
  getWaitPatient(1)
})
</script>
