import {extend} from "umi-request";


const request=extend({
  credentials:'include',
  //prefix:process.env.NODE_ENV==='production'? 'http://个人的域名或者ip地址':undefined
  prefix:process.env.NODE_ENV==='production'? 'http://82.157.244.162':undefined
});

export default request;
