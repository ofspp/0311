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
      <i class="el-icon-document-checked">病历首页</i>
    </div>
    <el-divider></el-divider>
    <el-descriptions title="病案首页"  :column="1" border style="margin-top:20px;width:80%">
      <el-descriptions-item label="主诉" :labelStyle="{'width':'120px'}">
        <el-input type="textarea" :autosize="{ minRows: 2, maxRows: 4}" v-model="medical_record.readme" placeholder="请输入主诉"></el-input>
      </el-descriptions-item>
      <el-descriptions-item label="现病史">
        <el-input type="textarea" :autosize="{ minRows: 2, maxRows: 4}" v-model="medical_record.present" placeholder="请输入现病史"></el-input>
      </el-descriptions-item>
      <el-descriptions-item label="现病治疗情况">
        <el-input type="textarea" :autosize="{ minRows: 2, maxRows: 4}" v-model="medical_record.present_treat" placeholder="请输入现病治疗情况"></el-input>
      </el-descriptions-item>
      <el-descriptions-item label="既往史">
        <el-input type="textarea" :autosize="{ minRows: 2, maxRows: 4}" v-model="medical_record.history" placeholder="请输入既往史"></el-input>
      </el-descriptions-item>
      <el-descriptions-item label="过敏史">
        <el-input type="textarea" :autosize="{ minRows: 2, maxRows: 4}" v-model="medical_record.allergy" placeholder="请输入过敏史"></el-input>
      </el-descriptions-item>
      <el-descriptions-item label="体格检查">
        <el-input type="textarea" :autosize="{ minRows: 2, maxRows: 4}" v-model="medical_record.physique" placeholder="请输入体格检查信息"></el-input>
      </el-descriptions-item>
      <el-descriptions-item label="诊断">
        <el-table :data="medical_record.disease" style="width:100%" @selection-change="delDiseaseSelection" ref="selectDiseaseTable">
          <el-table-column type="selection" width="55"></el-table-column>
          <el-table-column prop="disease_code" label="疾病编码" width="180"></el-table-column>
          <el-table-column prop="disease_name" label="疾病名称" width="180"></el-table-column>
          <el-table-column prop="diseaseICD" label="国际ICD编码" width="180"></el-table-column>
          <el-table-column prop="disease_category" label="疾病类型" width="180"></el-table-column>
          <el-table-column >
            <template #header>
              <el-button type="text" @click="delDisease">删除</el-button>
              <el-button type="text" @click="dialogTableVisible= true">增加</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-descriptions-item>
      <el-descriptions-item label="检查/检验建议">
        <el-input type="textarea" :autosize="{ minRows: 2, maxRows: 4}" v-model="medical_record.proposal" placeholder="请输入检查/检验建议"></el-input>
      </el-descriptions-item>
      <el-descriptions-item label="注意事项">
        <el-input type="textarea" :autosize="{ minRows: 2, maxRows: 4}" v-model="medical_record.careful" placeholder="请输入注意事项"></el-input>
      </el-descriptions-item>
    </el-descriptions>

    <div style="text-align:left;margin-top:20px">
      <el-button type="primary" @click="addNewMedicalRecord()">保存</el-button>
      <el-button type="primary" @click="clearMedicalRecord()">清空</el-button>
    </div>
    <!-- 疾病选择对话框 -->
    <el-dialog title="添加疾病诊断" v-model="dialogTableVisible">
      <div>
        <el-tag style="width:20%">疾病名称:</el-tag><el-input style="width:80%" v-model="disease.disease_name" placeholder="请输入疾病名称"></el-input>
      </div>
      <div>
        <el-tag style="width:20%">疾病助记码:</el-tag><el-input style="width:80%" v-model="disease.disease_code" placeholder="请输入疾病助记码"></el-input>
      </div>
      <div>
        <el-tag style="width:20%">疾病ICD:</el-tag><el-input style="width:80%" v-model="disease.diseaseICD" placeholder="请输入疾病国际ICD编码"></el-input>
      </div>
      <div style="float:right">
        <el-button @click="searchDisease()">搜索</el-button>
      </div>
      <el-table :data="search_disease" @selection-change="diseaseSelectionChange" :key="dialogTableVisible">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column property="disease_name" label="疾病名称" width="150"></el-table-column>
        <el-table-column property="disease_code" label="疾病助记码" width="200"></el-table-column>
        <el-table-column property="diseaseICD" label="疾病ICD"></el-table-column>
      </el-table>
      <div>
        <el-button type="primary" @click="addSelectionDisease()">添加</el-button>
        <el-button type="info" @click="dialogTableVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>

  
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { isNullMessage} from '@/util/util';
import {ElMessage,ElMessageBox} from 'element-plus';
import request from '@/util/util'
import qs from 'qs'

// 数据定义
const temp_disease = ref([]);             // 临时保存疾病信息
const search_disease = ref([]);           // 对话框搜索得到的疾病信息
const disease = reactive({});             // 搜索疾病条件
const dialogTableVisible = ref(false);    // 对话框显示控制
const medical_record = reactive({
  id: '',
  readme: '',           // 主诉
  present: '',          // 现病史
  present_treat: '',    // 现病治疗情况
  history: '',          // 既往史
  allergy: '',          // 过敏史
  physique: '',         // 体格检查
  disease: [],          // 诊断（对话框选择后的疾病信息）
  proposal: '',         // 检查、检验建议
  careful: '',          // 注意事项
});

// 方法定义
function delDiseaseSelection(selVal) {
  temp_disease.value = selVal;
}

function delDisease() {
  for (let i = 0; i < temp_disease.value.length; i++) {
    for (let j = 0; j < medical_record.disease.length; j++) {
      if (temp_disease.value[i].disease_name === medical_record.disease[j].disease_name) {
        medical_record.disease.splice(j, 1);
        j--;
      }
    }
  }
}

function diseaseSelectionChange(selVal) {
  temp_disease.value = selVal;
}

function addSelectionDisease() {
  if (temp_disease.value && temp_disease.value.length > 0) {
    temp_disease.value.forEach(item => {
      medical_record.disease.push(item);
    });
    dialogTableVisible.value = false
  }
}

async function searchDisease() {
  try {
    const resp = await request.get('/getDisease', disease);
    console.log('疾病-->' +resp.data)
    search_disease.value = resp.data;
  } catch (error) {
    console.error('搜索疾病失败', error);
  }
}

async function addNewMedicalRecord() {
  try {
    medical_record.register_id = patient.value.id;
    await request.post('/addHomeMedicalRecord', qs.stringify(medical_record));
    // 成功提示
    ElMessage({
      message: '添加病例成功！',
      type: 'success',
    });
    clearMedicalRecord();
  } catch (error) {
    console.error('添加病历失败', error);
  }
}

function clearMedicalRecord() {
  Object.assign(medical_record, {
    id: '',
    readme: '',
    present: '',
    present_treat: '',
    history: '',
    allergy: '',
    physique: '',
    disease: [],
    proposal: '',
    careful: '',
  });
}

///1.定义响应式 patient 对象
const patient = ref({
  id : '',
  real_name: '',
  case_number: '',
  age: '',
  gender: ''
})

//2.页面加载获取患者信息
onMounted(() => {
  const storedPatient = JSON.parse(sessionStorage.getItem('record_now_patient'))
  console.log('storedPatient JSON:', JSON.stringify(storedPatient))
  if (isNullMessage(storedPatient, '请先选择患者')) {
    patient.value = {
      id: '',
      real_name: '',
      case_number: '',
      age: '',
      gender: ''
    }
    
  } else {
    patient.value = storedPatient
    searchDisease()
  }
 
})
</script>
