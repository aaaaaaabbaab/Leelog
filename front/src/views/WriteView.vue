<script setup lang="ts">
import {ref} from "vue";
import * as url from "url";

//https://element-plus.org/en-US/guide/quickstart.html  참고
//https://axios-http.com/kr/docs/intro 참고
//https://getbootstrap.com/ 참고

import axios from 'axios';

// const count = ref(0);
// export default defineComponent({
// })
const title = ref("");
const content = ref("");
const writes = function (){
  // alert(title + " / " + content)
  axios.post("http://localhost:8080/posts",{title:title.value, content:content.value});
  console.log(title.value,content.value);
}
</script>

<template>
  <div>
    <el-input v-model="title" type="text" placeholder="제목을 입력해주세요"/>
  </div>

  <div class="mt-2">
    <div>
    <el-input v-model="content" type="textarea" rows="15"></el-input>
    </div>

    <div class="mt-2">
    <el-button type="primary" @click="writes()">글 작성완료</el-button>
    </div>
  </div>


<!--<p>안녕하세요</p> <button @click="count += 1">버튼을 눌러주세요</button>-->
<!--  <p>{{count}}</p>-->
</template>

<style>

</style>