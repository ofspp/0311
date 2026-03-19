<template>
  <div>
    <div style="text-align:left">
      患者信息：
      <el-tag >姓名：{{patient.real_name}}</el-tag>
      <el-tag >病历号：{{patient.case_number}}</el-tag>
      <el-tag >年龄：{{patient.age}}</el-tag>
      <el-tag >性别：{{patient.gender}}</el-tag>
    </div>
    <el-divider></el-divider>
    <div style="font-size:20px;text-align:left">
      <i class="el-icon-document-checked">在诊患者</i>
    </div>
    <el-divider></el-divider>
    <el-row :gutter="20">
      <el-col :span="6"><el-input v-model="finishCheckPatient.case_number" placeholder="请输入患者病历号"></el-input></el-col>
      <el-col :span="6"><el-input v-model="finishCheckPatient.real_name" placeholder="请输入患者姓名"></el-input></el-col>
      <el-col :span="2"><el-button @click="getPatient()">搜索</el-button></el-col>
      <el-col :span="10"> </el-col>
    </el-row>
    <el-table :data="now_all_patient" style="width:80%;margin-top:10px">
      <el-table-column type="index" label="编号" width="50"></el-table-column>
      <el-table-column prop="case_number" label="患者病历号" width="150"></el-table-column>
      <el-table-column prop="real_name" label="患者姓名" width="150"></el-table-column>
      <el-table-column prop="card_number" label="患者身份证" ></el-table-column>
      <el-table-column prop="visit_state" label="患者状态" width="120"></el-table-column>
      <el-table-column prop="visit_date" label="挂号时间" width="260"></el-table-column>
      <el-table-column label="操作" width="120">
        <template #default="scope" >
          <el-button size="mini" @click="toUpdateMedicalRecord(scope.$index,scope.row)">更新病历</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div style="width:80%;margin-top:10px;text-align:right">
    <el-pagination :total="totalCount" layout="prev,pager,next" 
      :page-size="pageSize" @current-change="nowAllPatientPageChange" >
    </el-pagination>
    </div>

  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue';
import qs from 'qs';
import { getSessionLoginUser, isNullMessage} from '@/util/util';
import request from '@/util/util'

// 分页、搜索条件
const finishCheckPatient = reactive({
  employee_id: '1',
  case_number: '',
  real_name: '',
  nowPageNumber: 1,
  pageSize: 10,
});

// 当前选择的患者信息
const patient = reactive({
  real_name: '',
  case_number: '',
  age: '',
  gender: '',
});

// 所有患者信息列表
const nowAllPatient = ref([]);

// 总记录数
const totalCount = ref(0);

// 页面大小
const pageSize = ref(10);

// 获取患者信息（分页/搜索）
const getPatient = async () => {
  try {
    const resp = await request.post('/getCheckedPatient', qs.stringify(finishCheckPatient));
    nowAllPatient.value = resp.data.list;
    totalCount.value = resp.data.totalCount;
  } catch (err) {
    console.error(err);
  }
};

// 更新病历（选择患者）
const toUpdateMedicalRecord = (index, row) => {
  sessionStorage.setItem('record_now_patient', JSON.stringify(row));
  patient.real_name = row.real_name;
  patient.case_number = row.case_number;
  patient.age = row.age;
  patient.gender = row.gender;
  window.$message?.({
    message: '选择患者：' + row.real_name,
    type: 'success',
  });
};

// 分页变化
const nowAllPatientPageChange = (val) => {
  finishCheckPatient.nowPageNumber = val;
  getPatient();
};

// 初始化登录用户信息
const initLoginUserInfo = () => {
  const userInfo = getSessionLoginUser();
  finishCheckPatient.employee_id = userInfo.id;
};

// 页面初始化
onMounted(() => {
  initLoginUserInfo();
  getPatient();

  const savedPatientStr = sessionStorage.getItem('record_now_patient');
  const savedPatient = savedPatientStr ? JSON.parse(savedPatientStr) : null;
  
  if (isNullMessage(savedPatient, '请先选择患者')) {
    Object.assign(patient, {
      real_name: '',
      case_number: '',
      age: '',
      gender: '',
    });
  } else {
    Object.assign(patient, savedPatient);
  }
});
</script>
