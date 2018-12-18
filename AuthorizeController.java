package com.octopus.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import com.jfinal.core.Controller;
import com.jfinal.kit.LogKit;
import com.octopus.web.conf.Pub;
import com.octopus.web.model.RecRecipe;
import com.octopus.web.model.RecRecipeDtl;
import com.octopus.web.model.SysUser;

public class AuthorizeController extends Controller {

  public void view(){
    
    setAttr("recipeId", getPara("recipeId"));
    renderJsp("authorize.jsp");
  }
  
  public void auth(){
    LogKit.debug(">>>>>> 师傅审核配方！");
    Map<String,Object> resp = new HashMap<>();
    
    String loginName = getPara("loginName");
    String password = getPara("password");
    String recipeId = getPara("recipeId");
    
    if(StringUtils.isBlank(loginName)){
      resp.put(Pub.RE_CODE_KEY, "01");
      resp.put(Pub.RE_MSG_KEY, "审核失败，账号不能为空！");
      renderJson(resp);
      return ;
    }
    if(StringUtils.isBlank(password)){
      resp.put(Pub.RE_CODE_KEY, "02");
      resp.put(Pub.RE_MSG_KEY, "审核失败，密码不能为空！");
      renderJson(resp);
      return ;
    }
    if(StringUtils.isBlank(recipeId)){
      resp.put(Pub.RE_CODE_KEY, "03");
      resp.put(Pub.RE_MSG_KEY, "审核失败，未知的配方！");
      renderJson(resp);
      return ;
    }    
    
    SysUser _user = new SysUser().findByLoginName(loginName);
    if (null == _user) {
      resp.put(Pub.RE_CODE_KEY, "04");
      resp.put(Pub.RE_MSG_KEY, "账户不存在！");
      renderJson(resp);
      return ;
    } else if (!DigestUtils.md5Hex(password).equals(_user.getPASSWORD())){
      resp.put(Pub.RE_CODE_KEY, "05");
      resp.put(Pub.RE_MSG_KEY, "密码错误！");
      renderJson(resp);
      return ;
    }
    
    RecRecipe recipe = RecRecipe.dao.findById(recipeId);
    if(null == recipe){
      resp.put(Pub.RE_CODE_KEY, "06");
      resp.put(Pub.RE_MSG_KEY, "审核失败，配方不存在！");
      renderJson(resp);
      return ;
    } else if (recipe.getIsAudit() == 1){
      resp.put(Pub.RE_CODE_KEY, "07");
      resp.put(Pub.RE_MSG_KEY, "审核失败，配方已经审核，无需重复审核！");
      renderJson(resp);
      return ;
    }
    
    List<RecRecipeDtl> ls = RecRecipeDtl.dao.listByRecId(recipeId);
    if(null == ls || ls.size() < 1){
      resp.put(Pub.RE_CODE_KEY, "08");
      resp.put(Pub.RE_MSG_KEY, "审核失败，请至少添加一个品名！");
      renderJson(resp);
      return ;
    } else {
      for(RecRecipeDtl dtl : ls){
        if(StringUtils.isBlank(dtl.getRawId())){
          resp.put(Pub.RE_CODE_KEY, "09");
          resp.put(Pub.RE_MSG_KEY, "审核失败，请将品名信息填写完整！");
          renderJson(resp);
          return ;
        }
        if(dtl.getDOSAGE() == null){
          resp.put(Pub.RE_CODE_KEY, "10");
          resp.put(Pub.RE_MSG_KEY, "审核失败，请将品名信息填写完整！");
          renderJson(resp);
          return ;
        }
      }
    }
    
    recipe.setIsAudit(1);
    recipe.setAuditId(_user.getID());
    recipe.setAuditName(_user.getLoginName());
    recipe.save(_user);
    
    resp.put(Pub.RE_CODE_KEY, "00");
    resp.put(Pub.RE_MSG_KEY, "审核成功！");
    renderJson(resp);
  }
}
