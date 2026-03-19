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
      <i class="el-icon-document-checked">处置项目</i>
    </div>
    <el-divider></el-divider>

    <el-table :data="check_patient_table" style="width:80%;margin-top:5px" highlight-current-row @current-change="checkSelectionChange">
      <el-table-column type="index" width="50"></el-table-column>
      <el-table-column prop="item_code" label="处置编码"></el-table-column>
      <el-table-column prop="item_name" label="处置名称"></el-table-column>
      <el-table-column prop="item_format" label="处置规格"></el-table-column>
      <el-table-column prop="item_price" label="单价"></el-table-column>
      <el-table-column prop="item_type" label="费用分类"></el-table-column>
    </el-table>
    <div align="left">
      <el-tag>已选择的处置项目：{{ checkPatient.item_name }}</el-tag>
    </div>

    <el-descriptions title="确认处置科室和处置医生" :column="2" border style="margin-top:20px;width:50%">
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
      <el-button type="primary" @click="addCheckPatient()">开始处置</el-button>
      <el-button type="primary" @click="clearCheckPatient()">重置医生</el-button>
    </div>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue';
import {ElMessage,ElMessageBox} from 'element-plus'
import qs from 'qs';
import { isNullMessage} from '@/util/util';
import request from '@/util/util'

// 响应式数据
const checkPatient = reactive({
  disposal_employee_id: '',
  id: '',
  item_name: ''
});

const employee_id = ref('');       // 选择医生id
const check_employee = ref([]);    // 处置科室医生列表
const deptment_id = ref('');       // 选择科室id
const check_dept = ref([]);        // 处置科室列表
const check_patient_table = ref([]);// 患者的处置项目列表
const patient = reactive({});      // 患者信息

// 增加患者处置
const addCheckPatient = async () => {
  checkPatient.disposal_employee_id = employee_id.value;
  if (checkPatient.disposal_employee_id === '' || checkPatient.id === '') {
    ElMessage({
      message: '请选择医生和处置项目',
      type: 'warning'
    });
    return;
  }

  try {
    const resp = await request.post(
      '/updataDisposalPatient',
      qs.stringify(checkPatient)
    );
    ElMessage({
      message: resp.data.msg,
      type: 'success'
    });
    getCheckPatientTable();
  } catch (err) {
    console.error(err);
    ElMessage({
      message: '请求失败',
      type: 'error'
    });
  }
};

// 清空医生选择
const clearCheckPatient = () => {
  deptment_id.value = '';
  employee_id.value = '';
};

// 患者处置项目选择
const checkSelectionChange = (currentRow) => {
  checkPatient.id = currentRow.id;
  checkPatient.item_name = currentRow.item_name;
};

// 获取患者处置项目列表
const getCheckPatientTable = async () => {
  try {
    const resp = await request.get(`/getDisposalPatient?register_id=${patient.id}`);
    check_patient_table.value = resp.data;
  } catch (err) {
    console.error(err);
  }
};

// 获取处置科室
const getCheckDept = async () => {
  try {
    const resp = await request.get('/getDisposalPatientDept');
    check_dept.value = resp.data;
  } catch (err) {
    console.error(err);
  }
};

// 科室下拉列表改变
const deptChange = (deptVal) => {
  employee_id.value = '';
  getCheckEmployee(deptVal);
};

// 获取处置医生
const getCheckEmployee = async (deptment_id_val) => {
  try {
    const resp = await request.get(`/getDisposalPatientEmp?deptment_id=${deptment_id_val}`);
    check_employee.value = resp.data;
  } catch (err) {
    console.error(err);
  }
};

// 页面挂载
onMounted(() => {
  const patientData = sessionStorage.getItem('disposal_now_patient');
  if (patientData) {
    Object.assign(patient, JSON.parse(patientData));
  }

  if (isNullMessage(patient, '请先选择患者')) {
    Object.assign(patient, {
      real_name: '',
      case_number: '',
      age: '',
      gender: ''
    });
  } else {
    getCheckPatientTable();
    getCheckDept();
  }
});
</script>
