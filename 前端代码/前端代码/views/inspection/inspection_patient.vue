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
      <i class="el-icon-document-checked">检验项目</i>
    </div>
    <el-divider></el-divider>

    <el-table :data="check_patient_table" style="width:80%;margin-top:5px" highlight-current-row @current-change="checkSelectionChange">
      <el-table-column type="index" width="50"></el-table-column>
      <el-table-column prop="item_code" label="检验编码"></el-table-column>
      <el-table-column prop="item_name" label="检验名称"></el-table-column>
      <el-table-column prop="item_format" label="检验规格"></el-table-column>
      <el-table-column prop="item_price" label="单价"></el-table-column>
      <el-table-column prop="item_type" label="费用分类"></el-table-column>
    </el-table>
    <div align="left">
      <el-tag>已选择的检验项目：{{ checkPatient.item_name }}</el-tag>
    </div>

    <el-descriptions title="确认检验科室和检验医生" :column="2" border style="margin-top:20px;width:50%">
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
      <el-button type="primary" @click="addCheckPatient()">开始检验</el-button>
      <el-button type="primary" @click="clearCheckPatient()">重置医生</el-button>
    </div>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { isNullMessage} from '@/util/util'
import request from '@/util/util'
// 检验患者对象
const checkPatient = reactive({
  inspection_employee_id: '',
  id: '',
  item_name: ''
})

const employee_id = ref('')           // 选择医生id
const check_employee = ref([])        // 检验科室医生列表
const deptment_id = ref('')           // 选择科室id
const check_dept = ref([])            // 检验科室列表
const check_patient_table = ref([])   // 患者的检验项目列表
const patient = ref({})          // 患者信息

// 增加患者检验
const addCheckPatient = async () => {
  checkPatient.inspection_employee_id = employee_id.value
  if (!checkPatient.inspection_employee_id || !checkPatient.id) {
    ElMessage.warning('请选择医生和检验项目')
    return
  }

  try {
    const resp = await request.post('/updataInspectionPatient', checkPatient)
    ElMessage.success(resp.msg)
    getCheckPatientTable()
  } catch (error) {
    console.error(error)
    ElMessage.error('提交失败')
  }
}

// 清空医生选择
const clearCheckPatient = () => {
  deptment_id.value = ''
  employee_id.value = ''
}

// 患者检验项目选择
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

// 得到患者的检验项目
const getCheckPatientTable = async () => {
  console.log('patientid:' + patient.value.id )
  try {
    const resp = await request.get('/getInspectionPatient', {
      params: { register_id: patient.value.id }
    })
    check_patient_table.value = resp.data
  } catch (error) {
    console.error(error)
  }
}

// 得到检验科室
const getCheckDept = async () => {
  try {
    const resp = await request.get('/getInspectionPatientDept')
    check_dept.value = resp.data
  } catch (error) {
    console.error(error)
  }
}

// 科室下拉列表改变
const deptChange = (deptVal) => {
  employee_id.value = ''
  getCheckEmployee(deptVal)
}

// 得到检验医生
const getCheckEmployee = async (deptId) => {
  try {
    const resp = await request.get('/getInspectionPatientEmp', {
      params: { deptment_id: deptId }
    })
    check_employee.value = resp.data
  } catch (error) {
    console.error(error)
  }
}




// 生命周期：初始化数据
onMounted(() => {
  const patientData = JSON.parse(sessionStorage.getItem('inspection_now_patient'))
 
  
  if (isNullMessage(patientData, '请先选择患者')) {
    Object.assign(patient, {
      real_name: '',
      case_number: '',
      age: '',
      gender: ''
    })    
  } else {        
    patient.value = patientData
    getCheckPatientTable()
    getCheckDept()
  }
})
</script>
