<template>
  <div >
    
    <div style="text-align:left">
      患者信息：
      <el-tag>姓名:{{patient.real_name}}</el-tag>
      <el-tag>病历号:{{patient.case_number}}</el-tag>
      <el-tag>年龄:{{patient.age}}</el-tag>
      <el-tag>性别:{{patient.gender}}</el-tag>
    </div>
    <el-divider></el-divider>
    <div style="font-size:20px;text-align:left">
      <i class="el-icon-document-checked">检查项目</i>
    </div>
    <el-divider></el-divider>

    <el-table :data="check_patient_table" style="width:80%;margin-top:5px" highlight-current-row @current-change="checkSelectionChange">
      <el-table-column type="index" width="50"></el-table-column>
      <el-table-column prop="item_code" label="检查编码"></el-table-column>
      <el-table-column prop="item_name" label="检查名称"></el-table-column>
      <el-table-column prop="item_format" label="检查规格"></el-table-column>
      <el-table-column prop="item_price" label="单价"></el-table-column>
      <el-table-column prop="item_type" label="费用分类"></el-table-column>
    </el-table>
    <div align="left">
      <el-tag>已选择的检查项目：{{ checkPatient.item_name }}</el-tag>
    </div>

    <el-descriptions title="确认检查科室和检查医生" :column="2" border style="margin-top:20px;width:50%">
      <el-descriptions-item label="科室选择">
        <el-select v-model="deptment_id" placeholder="选择科室" @change="deptChange">
          <el-option v-for="item in check_dept" :key="item.id" :label="item.dept_name" :value="item.id">
          </el-option>
        </el-select>
      </el-descriptions-item>
      <el-descriptions-item label="医生选择">
        <el-select v-model="employee_id" placeholder="选择医生">
          <el-option v-for="item in check_employee" :key="item.id" :label="item.realname" :value="item.id">
          </el-option>
        </el-select>
      </el-descriptions-item>
    </el-descriptions>
    <div style="text-align:left;margin-top:20px">
      <el-button type="primary" @click="addCheckPatient()">开始检查</el-button>
      <el-button type="primary" @click="clearCheckPatient()">重置医生</el-button>
    </div>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'
import qs from 'qs'
import { isNullMessage} from '@/util/util'
import {ElMessage,ElMessageBox} from 'element-plus'
import request from '@/util/util'

// 患者信息
const patient = ref({})

// 科室、医生、检查项等数据
const checkPatient = reactive({
  check_employee_id: '',
  id: '',
  item_name: ''
})

const employee_id = ref('')
const check_employee = ref([])
const deptment_id = ref('')
const check_dept = ref([])
const check_patient_table = ref([])

// 增加患者检查
const addCheckPatient = async () => {
  checkPatient.check_employee_id = employee_id.value

  if (!checkPatient.check_employee_id || !checkPatient.id) {
    return ElMessage({ message: '请选择医生和检查项目', type: 'warning' })
  }
  try {
    const resp = await request.post(
      '/updataCheckPatient',
      qs.stringify(checkPatient)
    )
    ElMessage({ message: resp.data.msg, type: 'success' })
    getCheckPatientTable()
  } catch (err) {
    console.error(err)
  }
}

// 清空医生选择
const clearCheckPatient = () => {
  deptment_id.value = ''
  employee_id.value = ''
}

// 患者检查项目选择
const checkSelectionChange = (currentRow) => {
  if (!currentRow) {
    checkPatient.id = ''   // 或 null
    checkPatient.item_name = ''
    return
  }
  checkPatient.id = currentRow.id
  checkPatient.item_name = currentRow.item_name
  console.log('checkPatient.id....', checkPatient.id)
}

// 获取患者检查项目列表
const getCheckPatientTable = async () => {
  try {
    const resp = await request.get(`/getCheckPatient?register_id=${patient.value.id}`)
    check_patient_table.value = resp.data
  } catch (err) {
    console.error(err)
  }
}

// 获取检查科室列表
const getCheckDept = async () => {
  try {
    const resp = await request.get('/getCheckPatientDept')
    check_dept.value = resp.data
  } catch (err) {
    console.error(err)
  }
}

// 科室下拉列表改变
const deptChange = (deptVal) => {
  employee_id.value = ''
  getCheckEmployee(deptVal)
}

// 获取科室医生列表
const getCheckEmployee = async (deptment_id_val) => {
  try {
    const resp = await request.get(`/getCheckPatientEmp?deptment_id=${deptment_id_val}`)
    check_employee.value = resp.data
  } catch (err) {
    console.error(err)
  }
}

// 页面加载时初始化
onMounted(() => {
  const nowPatient = JSON.parse(sessionStorage.getItem('check_now_patient'))
  if (isNullMessage(nowPatient, '请先选择患者')) {
    patient.value = { real_name: '', case_number: '', age: '', gender: '' }
  } else {
    patient.value = nowPatient
    getCheckPatientTable()
    getCheckDept()
  }
})
</script>

