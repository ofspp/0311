<template>
  <div >
    <div style="font-size:20px;text-align:left">
      <i class="el-icon-document-checked">检验结果录入</i>
    </div>
    <el-divider></el-divider>

    <el-row :gutter="20">
      <el-col :span="6"><el-input v-model="input_patient_id" placeholder="请输入患者病历号"></el-input></el-col>
      <el-col :span="6"><el-input v-model="input_patient_name" placeholder="请输入患者姓名"></el-input></el-col>      
      <el-col :span="2"><el-button @click="getWaitCheck(1)">搜索</el-button></el-col>
      <el-col :span="10"> </el-col>
    </el-row>
    
    <el-table
      :data="wait_check"      style="margin-top:5px;width: 80%">
      <el-table-column        type="index"     label="编号"        width="80"></el-table-column>
      <el-table-column        prop="real_name"        label="患者姓名"        width="180"></el-table-column>
      <el-table-column        prop="case_number"        label="患者病历号">     </el-table-column>
      <el-table-column label="操作">
      <template #default="scope">
        <el-button   size="mini" @click="createCheckApply(scope.$index, scope.row)">已完成检验</el-button>
      </template>
    </el-table-column>
    </el-table>
    <div style="margin-top:5px;text-align:right;width:80%">
      <el-pagination 
      :page-size="pageSize" :total="totalCount" @current-change="wait_patient_table_change"
      layout="prev, pager, next">
    </el-pagination>
    </div>
    <el-divider></el-divider>

    <el-table :data="check_patient_table" style="width:80%;margin-top:20px" highlight-current-row @current-change="checkSelectionChange">
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
    <el-divider></el-divider>

    <el-descriptions title="选择输入检验医生" :column="2" border style="margin-top:20px;width:50%">
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

    <el-descriptions title="输入检验结果" :column="1" border style="margin-top:20px;width:80%">
      <el-descriptions-item label="检验结果" :labelStyle="{'width':'120px'}">
        <el-input type="textarea" :autosize="{ minRows: 2, maxRows: 4}" v-model="proposal" placeholder="请输入检验结果"></el-input>
      </el-descriptions-item>
      
    </el-descriptions>
    <div style="text-align:left;margin-top:20px">
      <el-button type="primary" @click="addCheckPatient()">结果提交</el-button>
      <el-button type="primary" @click="clearCheckPatient()">重置输入</el-button>
    </div>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { isNullMessage} from '@/util/util'
import request from '@/util/util'
// 患者信息
const patient = ref({})

// 表单对象
const checkPatient = reactive({
  inspection_employee_id: '',
  id: '',
  item_name: ''
})

// 选择医生、科室相关
const employee_id = ref('')       // 选择医生id
const deptment_id = ref('')       // 选择科室id
const check_employee = ref([])    // 医生列表
const check_dept = ref([])        // 科室列表
const check_patient_table = ref([]) // 患者检查项目列表

// 增加患者检查
const addCheckPatient = async () => {
  checkPatient.inspection_employee_id = employee_id.value
  if (!checkPatient.inspection_employee_id || !checkPatient.id) {
    ElMessage.warning('请选择医生和检查项目')
  } else {
    const resp = await request.post('/updataInspectionPatient', checkPatient)
    ElMessage.success(resp.data.msg)
    getCheckPatientTable()
  }
}

// 清空医生选择
const clearCheckPatient = () => {
  deptment_id.value = ''
  employee_id.value = ''
}

// 选择检查项目
const checkSelectionChange = (currentRow) => {
  checkPatient.id = currentRow.id
  checkPatient.item_name = currentRow.item_name
}

// 得到患者的检查项目
const getCheckPatientTable = async () => {
  const resp = await request.get('/getInspectionPatient', {
    params: { register_id: patient.value.id }
  })
  check_patient_table.value = resp.data
}

// 得到检查科室
const getCheckDept = async () => {
  const resp = await request.get('/getInspectionPatientDept')
  check_dept.value = resp.data
}

// 科室下拉列表改变
const deptChange = (deptVal) => {
  employee_id.value = ''
  getCheckEmployee(deptVal)
}

// 得到检查医生
const getCheckEmployee = async (deptment_id) => {
  const resp = await request.get('/getInspectionPatientEmp', {
    params: { deptment_id }
  })
  check_employee.value = resp.data
}

onMounted(() => {
  patient.value = JSON.parse(sessionStorage.getItem('inspection_now_patient'))
  if (isNullMessage(this, patient.value, '请先选择患者')) {
    patient.value = {
      real_name: '',
      case_number: '',
      age: '',
      gender: ''
    }
  } else {
    getCheckPatientTable()
    getCheckDept()
  }
})
</script>
