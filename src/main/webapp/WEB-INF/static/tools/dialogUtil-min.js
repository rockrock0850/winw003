var DialogUtil=function(){var i,n,t="#007A55",e=".ui-dialog-titlebar",c=function(){i&&$(i).dialog().dialog("destroy"),HtmlUtil.lockSubmitKey(!1)};return{show:function(n,c){i=n;var o;o=$.extend({width:"1000px",maxWidth:"1000px",resizable:!1},c),$(i).dialog($.extend({modal:!0,position:{my:"center center",at:"center center"}},o)).prev(e).css("background-color",t),$(".ui-dialog").css("z-index",3),$(".ui-widget-overlay").css("z-index",2)},close:c,clickRow:function(i,n){n&&$(i+" tbody").unbind("click").on("click","tr",function(){n(TableUtil.getRow($(i).DataTable(),this)),c()})},showLoading:function(i){if(!n){var t={zIndex:10,overlay:$("#custom-overlay")};n=$("body").loading($.extend(t,i))}n.loading("start")},hideLoading:function(){n&&n.loading("stop")},findCheckedboxs:function(i){let n=[],t=$(i).DataTable(),e=$(i).find('input[type="checkbox"]');return $.each(e,function(){$(this).is(":checked")&&n.push(TableUtil.getRow(t,$(this)))}),n},findRadioChecked:function(i){var n,t=$(i).find('input[type="radio"]');return $.each(t,function(){if($(this).is(":checked"))return n=this,!1}),n},clickRowRadioEvent:function(i){var n;$(i).unbind("click").on("click","tr",function(){(n=$(this).find('input[type="radio"]').is(":checked"))||$(this).find('input[type="radio"]').prop("checked",!n)})},clickRowCheckboxEvent:function(i){var n;$(i).unbind("click").on("click","tr",function(){n=$(this).find('input[type="checkbox"]').is(":checked"),$(this).find('input[type="checkbox"]').prop("checked",!n).change()})}}}();