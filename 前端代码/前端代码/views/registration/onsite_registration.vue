<template>
  <div>
    <!-- 分割线组件 -->
    <el-divider></el-divider>
    <div style="font-size:20px;text-align:left">
      <i class="el-icon-document-checked">窗口挂号</i>
    </div>
    <el-divider></el-divider>
    <div style="text-align:left">
      <el-button type="primary" @click="addNewRegist()">挂号</el-button>
      <el-button type="primary" @click="clearRegist()">清空</el-button>
    </div>

    <el-descriptions title="基本信息"  :column="3" border style="margin-top:20px">
      <el-descriptions-item label="病历号">
        <el-input v-model="register.case_number" placeholder="请输入内容"></el-input>
      </el-descriptions-item>
      <el-descriptions-item label="姓名">
        <el-input v-model="register.real_name" placeholder="请输入姓名"></el-input>
      </el-descriptions-item>
      <el-descriptions-item label="性别">
        <el-select v-model="register.gender" placeholder="请选择">
          <el-option v-for="item in register_gender"  :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </el-descriptions-item>
      <el-descriptions-item label="年龄">
        <template>
          <el-input v-model="register.age" placeholder="请输入年龄" style="width:100px"></el-input>
          <el-select v-model="register.age_type" placeholder="请选择" style="width:80px">
            <el-option v-for="item in register_ageType"  :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </template>
      </el-descriptions-item>
      <el-descriptions-item label="出生日期">
        <el-date-picker v-model="register.birthday" type="date"  placeholder="选择日期" value-format="yyyy-MM-dd">
        </el-date-picker>
      </el-descriptions-item>
      <el-descriptions-item label="身份证号">
        <el-input v-model="register.card_number" placeholder="请输入身份证号"></el-input>
      </el-descriptions-item>
      <el-descriptions-item label="家庭住址">
        <el-input v-model="register.home_address" placeholder="请输入家庭住址（可选）"></el-input>
      </el-descriptions-item>
    </el-descriptions>

    <el-descriptions title="挂号信息" :column="3" border style="margin-top:20px">
      <el-descriptions-item label="结算类别">
        <el-select v-model="register.settle_id" placeholder="请选择">
          <el-option v-for="item in register_settle"  :key="item.id" :label="item.settle_name" :value="item.id">
          </el-option>
        </el-select>
      </el-descriptions-item>
      <el-descriptions-item label="看诊日期" >
        <el-date-picker disabled v-model="register.visit_date" type="date" placeholder="选择日期" value-format="yyyy-MM-dd">
        </el-date-picker>
      </el-descriptions-item>
      <el-descriptions-item label="午别" >
        <el-select disabled v-model="register.noon" placeholder="请选择">
          <el-option v-for="item in register_noon"  :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </el-descriptions-item>
      <el-descriptions-item label="挂号科室">
        <el-select v-model="register.deptment_id" placeholder="请选择">
          <el-option v-for="item in register_dept"  :key="item.id" :label="item.dept_name" :value="item.id">
          </el-option>
        </el-select>
      </el-descriptions-item>
      <el-descriptions-item label="号别">
        <el-select v-model="register.regist_level_id" placeholder="请选择" @change="registerLevelChange">
          <el-option v-for="item in register_level"  :key="item.id" :label="item.regist_name" :value="item.id">
          </el-option>
        </el-select>
      </el-descriptions-item>
      <el-descriptions-item label="看诊医生">
        <el-select v-model="register.employee_id" placeholder="请选择" @change="getAlreadyRegisterCount">
          <el-option v-for="item in register_employee"  :key="item.id" :label="item.realname" :value="item.id">
          </el-option>
        </el-select>
      </el-descriptions-item>
      <el-descriptions-item label="初始号额" >
        <el-input disabled v-model="register.total_number" placeholder="初始号额"></el-input>
      </el-descriptions-item>
      <el-descriptions-item label="已用号额">
        <el-input disabled v-model="register.used_number" placeholder="已用号额"></el-input>
      </el-descriptions-item>
      <el-descriptions-item label="病历本">
        <el-switch v-model="register.is_book" active-color="#13ce66" inactive-color="#ff4949" @change="registerIsbookChange">
        </el-switch>
      </el-descriptions-item>
      <el-descriptions-item label="应收金额">
        <el-input disabled v-model="register.regist_money" placeholder="请输入金额"></el-input>
      </el-descriptions-item>
      <el-descriptions-item label="收费方式">
        <el-select v-model="register.regist_method" placeholder="请选择">
          <el-option v-for="item in register_method"  :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </el-descriptions-item>
    </el-descriptions>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue';
import qs from 'qs';
import { ElMessage } from 'element-plus';
import request from '../../util/util'; // 封装的 axios

// 数据
const register = reactive({
  id: '',
  case_number: '', // 病历号
  real_name: '', // 姓名
  gender: '男',
  card_number: '',
  birthday: '',
  age: '',
  age_type: '年',
  home_address: '',
  visit_date: '',
  noon: '上午',
  deptment_id: '',
  employee_id: '',
  regist_level_id: '',
  settle_category_id: '1',
  used_number: '0',
  is_book: false,
  regist_method: '医保卡',
  regist_money: '',
  visit_state: ''
});

const register_gender = ref([
  { value: '男', label: '男' },
  { value: '女', label: '女' }
]);

const register_ageType = ref([
  { value: '年', label: '年' },
  { value: '天', label: '天' }
]);

const register_method = ref([
  { value: '现金', label: '现金' },
  { value: '微信', label: '微信' },
  { value: '支付宝', label: '支付宝' },
  { value: '银行卡', label: '银行卡' },
  { value: '医保卡', label: '医保卡' }
]);

const register_level = ref([]);
const register_employee = ref([]);
const register_noon = ref([
  { value: '上午', label: '上午' },
  { value: '下午', label: '下午' }
]);
const register_settle = ref([]);
const register_dept = ref([]);

// 方法

// 初始化当前时间
const initDateTime = () => {
  const nowDate = new Date();
  const nowDateString = `${nowDate.getFullYear()}-${nowDate.getMonth() + 1}-${nowDate.getDate()}`;
  register.visit_date = nowDateString;

  register.noon = nowDate.getHours() <= 12 ? '上午' : '下午';
};

// 清空挂号信息
const clearRegist = () => {
  register.id = '';
  register.case_number = '';
  register.real_name = '';
  register.gender = '男';
  register.card_number = '';
  register.birthday = '';
  register.age = '';
  register.age_type = '年';
  register.home_address = '';
  initDateTime();
  register.settle_id = '';
  register.deptment_id = '';
  register.employee_id = '';
  register.regist_level_id = '';
  register.settle_category_id = '1';
  register.is_book = false;
  register.used_number = '0';
  register.regist_method = '医保卡';
  register.regist_money = '';
  register.visit_state = '';
};

// 添加挂号信息
const addNewRegist = async () => {
  if (isNaN(parseInt(register.case_number))) {
    ElMessage({ message: '病例号必须为数字', type: 'error' });
    return;
  }

  try {
    const resp = await request.post('/addRegister', qs.stringify(register));
    ElMessage({ message: resp.data, type: 'success' });
    clearRegist();
  } catch (error) {
    console.error(error);
    ElMessage({ message: '添加挂号失败', type: 'error' });
  }
};

// 已挂号数量统计
const getAlreadyRegisterCount = async () => {
  try {
    const resp = await request.get(
      `/getAlreadyRegisterCount?visitDate=${register.visit_date}&noon=${register.noon}&employeeId=${register.employee_id}`
    );
    register.used_number = resp.data;
    if (register.used_number == register.total_number) {
      ElMessage({
        message: '警告，该医生号额已满，请选择其他医生',
        type: 'warning'
      });
    }
  } catch (error) {
    console.error(error);
  }
};

// 选择不同号别
const registerLevelChange = async (noonValue) => {
  register.is_book = false;
  const levelItem = register_level.value.find(item => item.sequence_no == noonValue);
  if (levelItem) {
    register.regist_money = levelItem.regist_fee;
    register.total_number = levelItem.regist_quota;
  }

  register.employee_id = '';

  try {
    const resp = await request.get(
      `/getRegistDoctorList?deptment_id=${register.deptment_id}&regist_level_id=${register.regist_level_id}`
    );
    register_employee.value = resp.data;
  } catch (error) {
    console.error(error);
  }
};

// 是否购买病历本
const registerIsbookChange = () => {
  register.regist_money = register.is_book
    ? register.regist_money + 1
    : register.regist_money - 1;
};

// 得到挂号级别列表
const getRegistLevelList = async () => {
  try {
    const resp = await request.get('/getRegistLevelList');
    register_level.value = resp.data;
  } catch (error) {
    console.error(error);
  }
};

// 得到部门信息
const getDeptList = async () => {
  try {
    const resp = await request.get('/getAllDeptList');
    
    register_dept.value = resp.data;
  } catch (error) {
    console.error(error);
  }
};

// 得到结算类别
const getSettleCategory = async () => {
  try {
    const resp = await request.get('/getSettleCategory');
    
    register_settle.value = resp.data;
   
  } catch (error) {
    console.error(error);
  }
};

// 得到最大病历
const getMaxCaseNumber = async () => {
  try {
    const resp = await request.get('/getMaxCaseNumber');
    register.case_number = parseInt(resp.data) + 1;
  } catch (error) {
    console.error(error);
  }
};

// 生命周期钩子
onMounted(() => {
  initDateTime();
  getRegistLevelList();
  getDeptList();
  getSettleCategory();
  getMaxCaseNumber();
});
</script>
