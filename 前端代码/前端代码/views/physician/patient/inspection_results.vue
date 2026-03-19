<template>
  <div>
    <div style="text-align:left">
      患者信息：
      <el-tag>姓名：{{patient.real_name}}</el-tag>
      <el-tag>病历号：{{patient.case_number}}</el-tag>
      <el-tag>年龄：{{patient.age}}</el-tag>
      <el-tag>性别：{{patient.gender}}</el-tag>
    </div>
    <el-divider></el-divider>
    <div style="font-size:20px;text-align:left">
      <i class="el-icon-document-checked">检验结果：</i>
    </div>
    <el-divider></el-divider>
    <el-table :data="check_patient_table" style="width:80%"
      highlight-current-row @current-change="checkSelectChange">
      <el-table-column type="index" width="50"></el-table-column>
      <el-table-column prop="item_code" label="检查编码" width="280"></el-table-column>
      <el-table-column prop="item_name" label="检查名称" ></el-table-column>
      <el-table-column prop="item_format" label="规格" width="180"></el-table-column>
      <el-table-column prop="item_price" label="单价" width="180"></el-table-column>
      <el-table-column prop="check_state" label="状态" width="180"></el-table-column>
      <el-table-column prop="item_type" label="费用分类" width="180"></el-table-column>
    </el-table>
    <el-divider></el-divider>
    <el-descriptions title="检查结果详情：" :column="1" border style="width:80%">
      <el-descriptions-item label="开立时间：" :contentStyle="{'background-color':'#F4F4F5'}" :labelStyle="{'width':'120px'}">
        <el-tag type="info">{{check_info.creation_time}}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="检查医生：" :contentStyle="{'background-color':'#F4F4F5'}" :labelStyle="{'width':'120px'}">
        <el-tag type="info">{{check_info.check_employee_name}}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="检查部位：" :contentStyle="{'background-color':'#F4F4F5'}" :labelStyle="{'width':'120px'}">
        <el-tag type="info">{{check_info.check_position}}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="目的要求：" :contentStyle="{'background-color':'#F4F4F5'}" :labelStyle="{'width':'120px'}">
        <el-tag type="info">{{check_info.check_info}}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="医嘱备注：" :contentStyle="{'background-color':'#F4F4F5'}" :labelStyle="{'width':'120px'}">
        <el-tag type="info">{{check_info.remark}}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="检查结果：" :contentStyle="{'background-color':'#F4F4F5'}" :labelStyle="{'width':'120px'}">
        <el-tag type="info">{{check_info.check_result}}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="检查时间：" :contentStyle="{'background-color':'#F4F4F5'}" :labelStyle="{'width':'120px'}">
        <el-tag type="info">{{check_info.check_time}}</el-tag>
      </el-descriptions-item>
    </el-descriptions>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue';
import { isNullMessage} from "../../../util/util";
import request from '@/util/util'

// 响应式数据
const patient = ref({});
const check_patient_table = ref([]);
const check_info = ref({});

// 方法：获取检查信息 table 数据
const getCheckPatientTableByRegist = async (registId) => {
  try {
    const resp = await request.get(`/getCheckPatientTableByRegist?registid=${registId}`);
    check_patient_table.value = resp.data;
  } catch (error) {
    console.error('获取检查信息失败', error);
  }
};

// 方法：选中检查信息
const checkSelectChange = (val) => {
  check_info.value = val;
  console.log(check_info.value.creation_time);
};

// 生命周期
onMounted(() => {
  const storedPatient = sessionStorage.getItem("record_now_patient");
  patient.value = storedPatient ? JSON.parse(storedPatient) : {};

  if (isNullMessage(patient.value, '请先选择患者')) {
    patient.value = {
      real_name: '',
      case_number: '',
      age: '',
      gender: ''
    };
  } else {
    getCheckPatientTableByRegist(patient.value.id);
  }
});
</script>
