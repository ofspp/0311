<template>
  <div>
    <div style="font-size:20px;text-align:left">
      <i class="el-icon-document-checked">检查结果录入</i>
    </div>
    <el-divider></el-divider>

    <el-row :gutter="20">
      <el-col :span="6"><el-input v-model="finish_check_search.case_number" placeholder="请输入患者病历号"></el-input></el-col>
      <el-col :span="6"><el-input v-model="finish_check_search.real_name" placeholder="请输入患者姓名"></el-input></el-col>
      <el-col :span="2"><el-button @click="getFinishCheck()">搜索</el-button></el-col>
      <el-col :span="10"> </el-col>
    </el-row>
    <el-table style="margin-top:5px;width:80%" :data="finish_check_patient" size="mini">
      <el-table-column type="index" label="编号" width="80"></el-table-column>
      <el-table-column prop="real_name" label="患者姓名" width="180"></el-table-column>
      <el-table-column prop="case_number" label="患者病历号"></el-table-column>
      <el-table-column prop="age" label="患者年龄" width="180"></el-table-column>
      <el-table-column prop="gender" label="患者性别" width="180"></el-table-column>
      <el-table-column>
        <template #default="scope">
          <el-button size="mini" @click="selectCheckPatient(scope.$index,scope.row)">已完成检查</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div style="margin-top:5px;text-align:right;width:80%">
      <el-pagination :page-size="finish_check_search.pageSize" :total="totalCount" @current-change="finishCheckPatientChange"
      layout="prev,pager,next">
      </el-pagination>
    </div>
    <el-divider></el-divider>

    <el-table :data="finish_check_patient_select" style="width:80%;margin-top:20px;" size="mini"
      highlight-current-row @current-change="checkSelectionChange">
      <el-table-column type="index" width="50"></el-table-column>
      <el-table-column prop="item_code" label="检查编码"></el-table-column>
      <el-table-column prop="item_name" label="检查名称"></el-table-column>
      <el-table-column prop="check_time" label="检查时间" width="180"></el-table-column>
      <el-table-column prop="item_price" label="单价" width="180"></el-table-column>
      <el-table-column prop="item_type" label="费用分类" width="180"></el-table-column>
    </el-table>
    <div align="left">
      <el-tag >已选择的检查项目：{{ checkPatient.item_name }}</el-tag>
    </div>
    <el-divider></el-divider>

    <el-descriptions title="选择输入检查结果的医生" :column="2" border style="margin-top:20px;width:50%">
      <el-descriptions-item label="科室选择">
        <el-select v-model="deptment_id" placeholder="选择科室" @change="deptChange">
          <el-option v-for="item in check_dept" :key="item.id" :label="item.dept_name" :value="item.id"></el-option>
        </el-select>
      </el-descriptions-item>
      <el-descriptions-item label="医生选择">
        <el-select v-model="employee_id" placeholder="选择医生">
          <el-option v-for="item in check_employee" :key="item.id" :label="item.realname" :value="item.id"></el-option>
        </el-select>
      </el-descriptions-item>
    </el-descriptions>

    <el-descriptions title="输入检查结果" :column="1" border style="margin-top:20px;width:80%">
      <el-descriptions-item label="检查结果" :labelStyle="{'width':'120px'}">
        <el-input type="textarea" :autosize="{minRows:2,maxRows:4}" v-model="update_check_input.check_result" placeholder="请输入检查结果"></el-input>
      </el-descriptions-item>
    </el-descriptions>

    <div style="text-align:left;margin-top:20px">
      <el-button type="primary" @click="updateCheckInput()">结果提交</el-button>
      <el-button type="primary" @click="clearCheckInput()">重置输入</el-button>
    </div>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'
import request from '@/util/util'
import { ElMessage } from 'element-plus'
import qs from 'qs'

// 数据定义
const temp_select_check_patient = ref('') // 记录最后点击的患者
const finish_check_search = reactive({
  case_number: '',
  real_name: '',
  nowPageNumber: 1,
  pageSize: 5, // 每页显示患者数量
})

const update_check_input = reactive({
  check_result: '',
  id: '',
  employee_id: '',
})

const check_employee = ref([
  {
    id: 1,
    realname: '华佗',
  },
])

const check_dept = ref([
  {
    id: '1',
    dept_name: '心电图科',
  },
])

const employee_id = ref('')
const deptment_id = ref('')
const checkPatient = reactive({ item_name: '' })
const totalCount = ref(0)
const finish_check_patient_select = ref([])
const finish_check_patient = ref([
  {
    real_name: '张三',
    case_number: '100001',
  },
  {
    real_name: '李四',
    case_number: '100002',
  },
])

// 方法实现
const getFinishCheck = async () => {
  try {
    const resp = await request.post('/getFinishCheck', qs.stringify(finish_check_search))
    finish_check_patient.value = resp.data.list
    totalCount.value = resp.data.totalCount
  } catch (error) {
    console.error(error)
  }
}

const selectCheckPatient = (index, row) => {
  temp_select_check_patient.value = row.id
  getFinishCheckByRegistid(row.id)
}

const getFinishCheckByRegistid = async (id) => {
  try {
    const resp = await request.get(`/getFinishCheckByRegistid?id=${id}`)
    finish_check_patient_select.value = resp.data
  } catch (error) {
    console.error(error)
  }
}

// 选择检查项目
const checkSelectionChange = (val) => {
  if (val !== undefined && val !== null) {
    checkPatient.item_name = val.item_name
    update_check_input.id = val.id
  }
}

// 分页点击
const finishCheckPatientChange = (nowPage) => {
  finish_check_search.nowPageNumber = nowPage
  getFinishCheck()
}

// 科室变化
const deptChange = (deptVal) => {
  employee_id.value = ''
  getCheckEmployee(deptVal)
}

// 获取检查医生
const getCheckEmployee = async (deptment_id_val) => {
  try {
    const resp = await request.get(`/getCheckPatientEmp?deptment_id=${deptment_id_val}`)
    check_employee.value = resp.data
  } catch (error) {
    console.error(error)
  }
}

// 更新检查输入
const updateCheckInput = async () => {
  update_check_input.employee_id = employee_id.value
  try {
    const resp = await request.post('/updateCheckInput', qs.stringify(update_check_input))
    ElMessage({
      message: resp.data.msg,
      type: 'success',
    })
    getFinishCheckByRegistid(temp_select_check_patient.value)
    clearCheckInput()
  } catch (error) {
    console.error(error)
  }
}

// 清空检查输入
const clearCheckInput = () => {
  update_check_input.check_result = ''
  deptment_id.value = ''
  employee_id.value = ''
}

// 获取检查科室
const getCheckDept = async () => {
  try {
    const resp = await request.get('/getCheckPatientDept')
    check_dept.value = resp.data
  } catch (error) {
    console.error(error)
  }
}

// 生命周期钩子
onMounted(() => {
  getFinishCheck()
  getCheckDept()
})
</script>
