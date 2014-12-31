//v.1.2 build 80512
//Copyright DHTMLX LTD. http://www.dhtmlx.com
//You allowed to use this component or parts of it under GPL terms
//To use it on other terms or get Professional edition of the component please contact us at sales@dhtmlx.com
//选择维修部位组件
/**
 *	Build combobox from existing select control.	在已存在的select基础上生成combobox
 *
 *  @param   parent      {string} id of existing select control
 *  @param   size      {int } size of control, optional
 *  @return            {object} combobox object
 *  @type   public
 *  @topic   0
 *
 **/
function dhtmlXComboFromSelect_2(parent,size) {
	if(typeof(parent) == "string") {
		parent = document.getElementById(parent);	//获得父下拉菜单
	}
	size=size||parent.getAttribute("width")||(window.getComputedStyle?window.getComputedStyle(parent,null)["width"]:(parent.currentStyle?parent.currentStyle["width"]:0));
	if((!size)||(size == "auto")) {
		size = parent.offsetWidth || 100;	//设置宽度
	}
	var z = document.createElement("SPAN");
	parent.parentNode.insertBefore(z,parent);
	parent.style.display = 'none';
    var s_type = parent.getAttribute('opt_type');
	var w = new dhtmlXCombo_2(z, parent.name, size,s_type, parent.tabIndex);
	var x = new Array();
	var sel = 0;
	var text = w.getComboText();	//取当前输入框值---------------
	parent.parentNode.removeChild(parent);
	w.selectOption(sel,null,true); 
	if (parent.onchange) 
		w.attachEvent("onChange",parent.onchange);
	return w;
}

var dhtmlXCombo_2_optionTypes = [];

/**
 *	@desc: build combobox	生成combobox
 *	@param: parent - (string) id of existing object which will be used as container
 *	@param: name - (string) name of combobox - will be used in FORM
 *	@param: width - (int) size of combobox
 *	@param: tabIndex - (int) tab index, optional
 *	@type: public
 *	@topic: 0
 **/
function dhtmlXCombo_2(parent,name,width,optionType,tabIndex){
	if(typeof(parent) == "string") {
		parent = document.getElementById(parent);
	}
	this.dhx_Event();
	this.optionType = (optionType != window['undefined'] && dhtmlXCombo_2_optionTypes[optionType]) ? optionType : 'default';
	this._optionObject = dhtmlXCombo_2_optionTypes[this.optionType];
	this._disabled = false;
	if (!window.dhx_glbSelectAr) {
		window.dhx_glbSelectAr = new Array();
		window.dhx_openedSelect = null;
		window.dhx_SelectId = 1;
		dhtmlxEvent(document.body, "click", this.closeAll);
		dhtmlxEvent(document.body, "keydown", function(e){
			try{
				if((e || event).keyCode == 9) {
					window.dhx_glbSelectAr[0].closeAll();
				}
			}catch(e){} 
			return true; 
		});
	}
	if(parent.tagName == "SELECT") {
		dhtmlXComboFromSelect_2(parent);
	} else {
		this._createSelf(parent, name, width, tabIndex);
		dhx_glbSelectAr.push(this);
	}
}

/**
 *	@desc: change control size	设置宽度
 *	@param: new_size - (int) new size value
 *	@type: public
 *	@topic: 0
 **/
dhtmlXCombo_2.prototype.setSize = function(new_size) {
	this.DOMlist.style.width = new_size + "px";
	if(this.DOMlistF) {
		this.DOMlistF.style.width = new_size + "px";
	}
	this.DOMelem.style.width = new_size + "px";
	this.DOMelem_input.style.width = (new_size) + 'px';
};

/**
 *	@desc: switch between combobox and auto-filter modes	选择是否为过滤模式
 *	@param: mode - (boolean) enable filtering mode
 *	@param: url - (string) url for filtering from XML, optional
 *	@param: cache - (boolean) XML caching, optional
 *	@param: autosubload - (boolean) enable auto load additional suggestions on selecting last loaded option
 *	@type: public
 *	@topic: 0
 **/
dhtmlXCombo_2.prototype.enableFilteringMode = function(mode, url, cache, autosubload) {
	this._filter = convertStringToBoolean(mode);	//全局过滤开关
	if(url) {
		this._xml = url;
		this._autoxml = convertStringToBoolean(autosubload);
	}
	if (convertStringToBoolean(cache)) {
		this._xmlCache = [];
	}
};

/**
 *	@desc: set filter param		加入过滤参数
 *	@param: name - (string) param name
 *	@param: value - (string) param value
 *	@type: public
 *	@topic: 0
 **/
dhtmlXCombo_2.prototype.setFilteringParam = function(name, value){
	if(!this._prs) {
		this._prs = [];
	}
	this._prs.push([name,value]);
};

/**
 *	@desc: disable combobox		设置combobox是否无效化
 *	@param: mode - (boolean) disable combobox
 *	@type: public
 *	@topic: 1
 **/
dhtmlXCombo_2.prototype.disable = function(mode) {
	var z = convertStringToBoolean(mode);
	if (this._disabled == z) return;
	this.DOMelem_input.disabled = z;
	this._disabled = z;
};

/**
 *	@desc: switch to readonly mode	设置combobox是否为只读
 *	@param: mode - (boolean) readonly mode
 *	@param: autosearch - (boolean) true by default 
 *	@type: public
 *	@topic: 1
 **/
dhtmlXCombo_2.prototype.readonly = function(mode,autosearch) {
	this.DOMelem_input.readOnly = mode ? true : false;
	if(autosearch === false) {
		this.DOMelem.onkeyup = function(ev){};
	} else {
		var that = this;
		this.DOMelem.onkeyup = function(ev) { 
			ev = ev || window.event; 
			if (ev.keyCode!=9) {
				ev.cancelBubble = true;
			}
			if((ev.keyCode >= 48 && ev.keyCode <= 57)||(ev.keyCode >= 65 && ev.keyCode <= 90)) {
				for(var i=0; i<that.optionsArr.length; i++) {
					var text = that.optionsArr[i].text;	
					if(text.toString().toUpperCase().indexOf(String.fromCharCode(ev.keyCode)) == 0) {
						that.selectOption(i);
						break;
					}
				}
				ev.cancelBubble=true;
			}
		};
	}
};
   
/**
 *	@desc: get Option by value	根据键获得下拉项
 *	@param: value - (string) value of option in question
 *	@type: public
 *	@return: option object
 *	@topic: 2
 **/
dhtmlXCombo_2.prototype.getOption = function(value) {
	for(var i = 0; i < this.optionsArr.length; i++) {
		if(this.optionsArr[i].value == value) {
			return this.optionsArr[i];
		}
	}
	return null;
};

/**
 *	@desc: get Option by label	根据值获得下拉项
 *	@param: label - (string) label of option in question
 *	@type: public
 *	@return: option object
 *	@topic: 2
 **/
dhtmlXCombo_2.prototype.getOptionByLabel = function(value) {
	value = value.replace(/&/g,"&amp;");
	for(var i=0; i<this.optionsArr.length; i++) {
		if(this.optionsArr[i].text == value) {
			return this.optionsArr[i];
		}
	}
	return null;
};

/**
 *	@desc: get Option by index	根据排序获得下拉项
 *	@param: ind - (int) index of option in question
 *	@type: public
 *	@return: option object
 *	@topic: 2
 **/
dhtmlXCombo_2.prototype.getOptionByIndex = function(ind) {
	return this.optionsArr[ind];
};

/**
 *	@desc: clear all options from combobox	清空所有下拉项
 *	@type: public
 *	@param: value - (bool) clear current value as well
 *	@topic: 2
 **/
dhtmlXCombo_2.prototype.clearAll = function(all) {
	if(all) {
		this.setComboText("");
	}
	this.optionsArr = new Array();
	this.redrawOptions();
	if(all) {
		this._confirmSelection();
	}
};

/**
 *	@desc: delete option by value	根据键删除下拉项
 *	@param: value - (string) value of option in question
 *	@type: public
 *	@topic: 2
 **/
dhtmlXCombo_2.prototype.deleteOption = function(value) {
	var ind = this.getIndexByValue(value);
   	if(ind < 0) return;                            
   	if(this.optionsArr[ind] == this._selOption) {
   		this._selOption = null;
   	}
   	this.optionsArr.splice(ind, 1);
   	this.redrawOptions();
};

/**
 *	@desc: enable/disable immideatly rendering after changes in combobox	是否改变combobox后立刻渲染
 *	@param: mode - (boolean) enable/disable
 *	@type: public
 *	@topic: 1
 **/
dhtmlXCombo_2.prototype.render = function(mode) {
	this._skiprender = (!convertStringToBoolean(mode));
	this.redrawOptions();
};

/**
 *	@desc: update option in combobox	更新下拉项
 *	@param: oldvalue - (string) index of option in question
 *	@param: avalue - (variable)
 *	@type: public
 *	@topic: 2
 **/
dhtmlXCombo_2.prototype.updateOption = function(oldvalue, avalue, atext, acss) {
	var dOpt = this.getOption(oldvalue);
	if(typeof(avalue) != "object") {
		avalue = {text : atext, value : avalue, css : acss};
	}
	dOpt.setValue(avalue);
	this.redrawOptions();
};

/**
 *	@desc: add new option
 *	@param: value - (variable) - different input for different kinds of options - please refer to examples
 *	@type: public
 *	@topic: 2
 **/
dhtmlXCombo_2.prototype.addOption = function(options) {
	if (!arguments[0].length || typeof(arguments[0]) != "object") {		//加一条
		args = [arguments];
	} else {	//加多条
		args = options;
	}
	this.render(false);
	for (var i = 0; i < args.length; i++) {
		var attr = args[i];
		if (attr.length) {
			attr.value = attr[0] || "";
			attr.text = attr[1] || "";
			attr.css = attr[2] || "";
		}
		this._addOption(attr);
	}
	this.render(true);
};

dhtmlXCombo_2.prototype._addOption = function(attr) {
	dOpt = new this._optionObject();
	this.optionsArr.push(dOpt);
	dOpt.setValue.apply(dOpt,[attr]);
	this.redrawOptions();
};

/**
 *	@desc: return index of item by value	根据键获得下拉项位置
 *	@param: value - (string) value of option in question
 *	@type: public
 *	@return: option index
 *	@topic: 2
 **/
dhtmlXCombo_2.prototype.getIndexByValue = function(val) {
	for(var i=0; i<this.optionsArr.length; i++)
		if(this.optionsArr[i].value == val) return i;
	return -1;
};

/**
 *	@desc: code by myself	根据值获得下拉项位置
 *	@type: public
 *	@return: option index
 *	@topic: 2
 **/
dhtmlXCombo_2.prototype.getIndexByText = function(text) {
	for(var i = 0; i < this.optionsArr.length; i++) {
		if(this.optionsArr[i].data()[1] == text) {
			return i;
		}
	}
	return -1;
};

/**
 *	@desc: get value of selected item	获得已选中的键
 *	@type: public
 *	@return: option value
 *	@topic: 2 
 **/
dhtmlXCombo_2.prototype.getSelectedValue = function() {
	return (this._selOption ? this._selOption.value : null);
};

/**
 *	@desc: get current text in combobox	获得当前输入框内值
 *	@type: public
 *	@return: combobox text
 *	@topic: 2
 **/
dhtmlXCombo_2.prototype.getComboText = function() {
	return this.DOMelem_input.value;
};

/**
 *	@desc: set text in covmbobox	设定当前输入框内值
 *	@param: text - (string) new text label
 *	@type: public
 *	@topic: 2
 **/
dhtmlXCombo_2.prototype.setComboText = function(text) {
	this.DOMelem_input.value = text;
};
   
/**
 *	@desc: set value in covmbobox	根据键选中下拉项
 *	@param: text - (string) new text label
 *	@type: public
 *	@topic: 2
 **/
dhtmlXCombo_2.prototype.setComboValue = function(text) {
	this.setComboText(text);
	for(var i = 0; i < this.optionsArr.length; i++) {
		if(this.optionsArr[i].data()[0] == text) {
			return this.selectOption(i, null, true);      
		}
	}
	this.DOMelem_hidden_input.value = text;
};

/**
 *	@desc: get value which will be sent with form	获得实际值
 *	@type: public
 *	@return: combobox value
 *	@topic: 2
 *	@return DOMelem_hidden_input.value
 **/
dhtmlXCombo_2.prototype.getActualValue = function() {
	return this.DOMelem_hidden_input.value;
};

/**
 *	@desc: get text of selected option	获得已选中的值
 *	@type: public
 *	@return: text of option
 *	@topic: 2
 **/
dhtmlXCombo_2.prototype.getSelectedText = function() {
	return (this._selOption ? this._selOption.text : "");
};

/**
 *	@desc: get index of selected option	获得已选中的位置
 *	@type: public
 *	@return: option index
 *	@topic: 2
 **/
dhtmlXCombo_2.prototype.getSelectedIndex = function() {
	for(var i=0; i<this.optionsArr.length; i++) {
		if(this.optionsArr[i] == this._selOption) {
			return i;
		}
	}
	return -1;
};

/**
 *	@desc: create self HTML
 *	@type: private
 *	@topic: 0
 **/
dhtmlXCombo_2.prototype._createSelf = function(selParent, name, width, tab) {
	if (width.toString().indexOf("%") != -1) { 
		var self = this;
		var resWidht = parseInt(width) / 100;
		window.setInterval(function() { 
			var ts = selParent.parentNode.offsetWidth * resWidht - 2;
			if(ts==self._lastTs) return;
			self.setSize(self._lastTs=ts);
		},500);
		var width=parseInt(selParent.offsetWidth); //mm
	}
	var width = parseInt(width || 100); //mm
	this.ListPosition = "Bottom"; //set optionlist positioning
	this.DOMParent = selParent;
	this._inID = null;
	this.name = name;
	this._selOption = null; //selected option object pointer
	this.optionsArr = Array();
	var opt = new this._optionObject();
	opt.DrawHeader(this,name, width,tab);
	//HTML select part 2 - options list DIV element
	this.DOMlist = document.createElement("DIV");
	this.DOMlist.className = 'dhx_combo_list';
	this.DOMlist.style.width = width - (_isIE ? 0 : 0) + "px";
	if(_isOpera || _isKHTML ) {
		this.DOMlist.style.overflow = "auto";
	}
	this.DOMlist.style.display = "none";
	document.body.insertBefore(this.DOMlist,document.body.firstChild);         
	if(_isIE) {
		this.DOMlistF = document.createElement("IFRAME");
		this.DOMlistF.style.border = "0px";
		this.DOMlistF.className = 'dhx_combo_list';
		this.DOMlistF.style.width = width - (_isIE ? 0 : 0) + "px";
		this.DOMlistF.style.display = "none";
		this.DOMlistF.src = "javascript:false;"; 
		document.body.insertBefore(this.DOMlistF,document.body.firstChild);
	}
	this.DOMlist.combo = this.DOMelem.combo = this;
	this.DOMelem_input.onkeydown = this._onKey;
	this.DOMelem_input.onkeypress = this._onKeyF;
	this.DOMelem_input.onblur = this._onBlur;
	this.DOMelem.onclick = this._toggleSelect;
	this.DOMlist.onclick = this._selectOption;
	this.DOMlist.onmousedown = function() {
		this._skipBlur = true;
	};
	this.DOMlist.onkeydown = function(e) {
		this.combo.DOMelem_input.focus();
		(e || event).cancelBubble = true;
		this.combo.DOMelem_input.onkeydown(e);
	};
};

/**
 *	@desc: place option list in necessary place on screen
 *	@type: private
 *	@topic: 0
 **/
dhtmlXCombo_2.prototype._positList = function() {
	var pos = this.getPosition(this.DOMelem);
	if(this.ListPosition == 'Bottom') {
		this.DOMlist.style.top = pos[1] + this.DOMelem.offsetHeight + "px";
		this.DOMlist.style.left = pos[0] + "px";
	} else if(this.ListPosition == 'Top') { //mm
		this.DOMlist.style.top = pos[1] - this.DOMlist.offsetHeight + "px";
		this.DOMlist.style.left = pos[0] + "px"; //mm
	} else {
		this.DOMlist.style.top = pos[1] + "px";
		this.DOMlist.style.left = pos[0] + this.DOMelem.offsetWidth + "px";
	}
};
	  
/**
 *	@desc: get option list's place on screen
 *	@type: private
 *	@topic: 0
 **/
dhtmlXCombo_2.prototype.getPosition = function(oNode,pNode) {
	if(!pNode) {
		var pNode = document.body;
	}
	var oCurrentNode = oNode;
	var iLeft = 0;
	var iTop = 0;
	while ((oCurrentNode)&&(oCurrentNode!=pNode)) {	//.tagName!="BODY"){
		iLeft += oCurrentNode.offsetLeft - oCurrentNode.scrollLeft;
		iTop += oCurrentNode.offsetTop - oCurrentNode.scrollTop;
		oCurrentNode = oCurrentNode.offsetParent;	//isIE() ? : oCurrentNode.parentNode;
	}
	if(pNode == document.body) {
		if(_isIE) {
			if(document.documentElement.scrollTop) {
				iTop += document.documentElement.scrollTop;
			}
			if(document.documentElement.scrollLeft) {
				iLeft+=document.documentElement.scrollLeft;
			}
		} else if(!_isFF) {
			iLeft += document.body.offsetLeft;
			iTop += document.body.offsetTop;
		}
	}
	return new Array(iLeft,iTop);
};

/**
 *	@desc: correct current selection ( move it to first visible option )
 *	@type: private
 *	@topic: 2
 **/
dhtmlXCombo_2.prototype._correctSelection = function() {
	if(this.getComboText() != "") {
		for(var i = 0; i < this.optionsArr.length; i++) {
			if(!this.optionsArr[i].isHidden()) {
				return this.selectOption(i, true, false);
			}
		}
	}
	this.unSelectOption();
};

/**
 *	@desc: select next option in combobox
 *	@param: step - (int) step size
 *	@type: private
 *	@topic: 2
 **/
dhtmlXCombo_2.prototype.selectNext = function(step) {
	var z = this.getSelectedIndex() + step;
	while(this.optionsArr[z]) {
		if(!this.optionsArr[z].isHidden()) {
			return this.selectOption(z, false, false);
		}
		z += step;
	}
};

/**
 *	@desc: on keypressed handler 输入框中按下键事件只限有对应asc2码的键位（Shift、Ctrl、Alt、F1、F2、...等按键是不会产生onKeyPress事件的）
 *	@type: private
 *	@topic: 0
 **/
dhtmlXCombo_2.prototype._onKeyF = function(e) {
	var that = this.parentNode.combo;
	var ev = e||event;
	ev.cancelBubble = true;
	if(ev.keyCode == "13" || ev.keyCode == "9") {	//Enter || Tab
		that.selectNext(0);
		that._confirmSelection();
		that.closeAll();
	} else if(ev.keyCode == "27") {		//Esc
		that._resetSelection();
		that.closeAll();
	} else {
		that._activeMode = true;
	}
	if (ev.keyCode == "13" || ev.keyCode == "27") { //enter
		that.callEvent("onKeyPressed",[ev.keyCode]);
		return false;
	}
	return true;
};

/**
 *	@desc: on keydown handler	输入框中按下按键触发事件
 *	@type: private
 *	@topic: 0
 **/
dhtmlXCombo_2.prototype._onKey = function(e) {
	var that = this.parentNode.combo;
	(e || event).cancelBubble = true;
	var ev = (e || event).keyCode;
	if(ev > 15 && ev < 19) return true; //shift,alt,ctrl
	if(ev == 27) return;	//Esc
	if((that.DOMlist.style.display != "block") && (ev != "13") && (ev != "9") && ((!that._filter) || (that._filterAny))) {	//Enter || Tab
		that.DOMelem.onclick(e||event);
	}
	if((ev != "13") && (ev != "9")) {	//!Enter && !Tab
		window.setTimeout(function(){
			that._onKeyB(ev);
		}, 100);
		if(ev == "40" || ev == "38")	//up || down
			return false;
	} else if(ev == 9) {	
		that.closeAll();
		(e || event).cancelBubble = false;
	}
};

dhtmlXCombo_2.prototype._onKeyB = function(ev) {
	if(ev == "40") {  //down
		var z = this.selectNext(1);
	} else if (ev == "38") { //up
		this.selectNext(-1);
	} else {
		this.callEvent("onKeyPressed",[ev]);
		if (this._filter) {
			catchPositions(this.getComboText().toUpperCase());
		}
		for(var i = 0; i < this.optionsArr.length; i++) {
			if(this.optionsArr[i].data()[1] == this.DOMelem_input.value) {
				this.selectOption(i, false, false);
				return false;
			}
		}
		this.unSelectOption();
	}
	return true;
};

/**
 *	@desc: on data change handler
 *	@type: private
 *	@topic: 0
 **/
dhtmlXCombo_2.prototype._onBlur = function() {
	var self = this.parentNode._self;
	if(self.getIndexByText(self.getComboText()) == -1 && self.getComboText() != "") {	// && self.optionsArr.length != 1
		self.DOMelem_input.value = "";
		self.DOMelem_input.focus();
		return;
	}
	window.setTimeout(function() {
		if(self.DOMlist._skipBlur) {
			return !(self.DOMlist._skipBlur=false);
		}
		self._confirmSelection();        
		self.callEvent("onBlur", []);
	}, 100);
};

/**
 *	@desc: redraw combobox options by text	重新生成下拉项
 *	@type: private
 *	@topic: 2
 **/
dhtmlXCombo_2.prototype.redrawOptions = function() {
	if (this._skiprender) return;
	for(var i = this.DOMlist.childNodes.length - 1; i >= 0; i--) {
		this.DOMlist.removeChild(this.DOMlist.childNodes[i]);
	}
	for(var i = 0; i < this.optionsArr.length; i++) {
		if(this.optionsArr[i].text == "") {
			this.optionsArr[i].hide(true);
		}
		this.DOMlist.appendChild(this.optionsArr[i].render());
	}
};

/**
 *	@desc: deselect option	取消当前下拉项被选中状态
 *	@type: public
 *	@topic: 1
 **/
dhtmlXCombo_2.prototype.unSelectOption = function() {	
	if(this._selOption) {
		this._selOption.deselect();
	}
	this._selOption = null;
};
      
/**
 *	@desc: confirm select option	确认选中
 *	@param data
 *	@param status
 *	@type private
 **/
dhtmlXCombo_2.prototype._confirmSelection = function(data, status) {
	if(arguments.length == 0) {	//如果无参数
		var z = this.getOptionByLabel(this.DOMelem_input.value);	//根据当前输入框值获得下拉项
		data = z ? z.value : this.DOMelem_input.value;	//没找到则以输入框值为数据
		status = (z == null);
		if (data == this.getActualValue()) return;
	}
	this.DOMelem_hidden_input.value = data;
	this.DOMelem_hidden_input2.value = (status ? "true" : "false");
	this.callEvent("onChange",[]);
	this._activeMode = false;
};

dhtmlXCombo_2.prototype._resetSelection = function(data, status) {
	var z = this.getOption(this.DOMelem_hidden_input.value);
	this.setComboValue(z ? z.data()[0] : this.DOMelem_hidden_input.value);
	this.setComboText(z ? z.data()[1] : this.DOMelem_hidden_input.value);
};	  
  	  
/**
 *     @desc: select option	选中一个下拉项	ind:序号;	filter:	暂时无用;	conf:实选/预选
 *     @param: ind - (int) index of option in question
 *     @param: filter - (boolean) enable autocomplit range, optional
 *     @param: conf - (boolean) true for real selection, false for pre-selection
 *     @type: public
 *     @topic: 1
 **/
dhtmlXCombo_2.prototype.selectOption = function(ind, filter, conf) {	  		 
	if(arguments.length < 3) conf = true;	//默认conf = true
	this.unSelectOption();			
	var z = this.optionsArr[ind];	//获得根据位置获得下拉项
	if(!z) {
		return;
	}
	this._selOption = z;
	this._selOption.select();
	var corr = this._selOption.content.offsetTop + this._selOption.content.offsetHeight - this.DOMlist.scrollTop - this.DOMlist.offsetHeight;
	if(corr > 0) this.DOMlist.scrollTop += corr;
	corr = this.DOMlist.scrollTop - this._selOption.content.offsetTop;
	if(corr > 0) {
		this.DOMlist.scrollTop -= corr;
	}
	var data = this._selOption.data();
	if(conf) {
		this.setComboText(data[1]);
		this._confirmSelection(data[0], false);
	}	 			
	if((this._autoxml)&&((ind + 1) == this._lastLength)) {
		this._fetchOptions(ind + 1, this._lasttext || "");
	}
	if(filter) {
		var text = this.getComboText();			  
		if(text != data[1]) {
		}
	} else {
		this.setComboText(data[1]);
	}
	/*Event*/
	this.callEvent("onSelectionChange",[]);
};

/**
 *	@desc: option on select handler
 *	@type: private
 *	@topic: 2
 **/
dhtmlXCombo_2.prototype._selectOption = function(e) {
	(e||event).cancelBubble = true;
	var node = (_isIE ? event.srcElement : e.target);
	var that = this.combo;
	while(!node._self) {
		node = node.parentNode;
		if (!node)
			return;
	}
	var i = 0;
	for(i; i < that.optionsArr.length; i++) {
		if (that.optionsArr[i].text == node.innerHTML) break;
	}
//	for(i; i < that.DOMlist.childNodes.length; i++) {
//		if (that.DOMlist.childNodes[i]==node) break;
//	}
	that.selectOption(i, false, true);
	that.closeAll();
	that.callEvent("onBlur", []);
	that._activeMode = false;
};

/**
 *	@desc: open list of options 
 *	@type: public
 *	@topic: 2
 **/
dhtmlXCombo_2.prototype.openSelect = function() { 
	if(this._disabled) {
		return;
	}
	this.closeAll();
	this._positList();
	this.DOMlist.style.display = "block";
	this.callEvent("onOpen",[]);
	if (_isIE) this._IEFix(true);
	this.DOMelem_input.focus();
};

/**
 *	@desc: open(close) list
 *	@type: private
 *	@topic: 2
 **/
dhtmlXCombo_2.prototype._toggleSelect = function(e) {
	var that = this.combo;
	if(that.getSelectedText() != "") {	//点击时，有旧值则清空
		that.setComboText("");
	}
	if(that.DOMlist.style.display == "block") {
		that.closeAll();
	} else {
		that.openSelect();
	}
	(e||event).cancelBubble = true;
};

dhtmlXCombo_2.prototype._fetchOptions = function(ind,text) {
	if(text == "") {
		this.closeAll();
		return this.clearAll();
	}
	var url = this._xml + ((this._xml.indexOf("?") != -1) ? "&" : "?") + "pos=" + ind + "&mask=" + encodeURIComponent(text);
	this._lasttext = text;
	if(this._load) {
		this._load = url;
	} else {
		this.loadXML(url);
	}
};

/**
 *	@desc: set hidden iframe for IE
 *	@type: private
 *	@topic: 2
 **/
dhtmlXCombo_2.prototype._IEFix = function(mode) {
	this.DOMlistF.style.display = (mode ? "block" : "none");
	this.DOMlistF.style.top = this.DOMlist.style.top;
	this.DOMlistF.style.left = this.DOMlist.style.left;
};

/**
 *	@desc: close opened combobox list
 *	@type: public
 *	@topic: 1
 **/
dhtmlXCombo_2.prototype.closeAll = function() {
	if(window.dhx_glbSelectAr) {
		for (var i = 0; i < dhx_glbSelectAr.length; i++) {
			if(dhx_glbSelectAr[i].DOMlist.style.display == "block") {
				dhx_glbSelectAr[i].DOMlist.style.display = "none";
				if(_isIE) {
					dhx_glbSelectAr[i]._IEFix(false);
				}
			}
            dhx_glbSelectAr[i]._activeMode=false;
        }
	}
};

/**
 *	@desc: combobox option object constructor
 *	@type: public
 *	@topic: 0
 **/
dhtmlXCombo_2_defaultOption = function() {
	this.init();
};

/**
 *	@desc: option initialization function
 *	@type: private
 *	@topic: 4
 **/
dhtmlXCombo_2_defaultOption.prototype.init = function() {
	this.value = null;
	this.text = "";
	this.selected = false;
	this.css = "";
};

/**
 *	@desc: mark option as selected
 *	@type: public
 *	@topic: 4
 **/
dhtmlXCombo_2_defaultOption.prototype.select = function(){
	if (this.content) ;
	this.content.className = "dhx_selected_option";
};

dhtmlXCombo_2_defaultOption.prototype.hide = function(mode){
    this.render().style.display=mode?"none":"";
 };

dhtmlXCombo_2_defaultOption.prototype.isHidden = function(){
    return (this.render().style.display=="none");
 };



/**
 *	@desc: mark option as not selected
 *	@type: public
 *	@topic: 4
 **/
dhtmlXCombo_2_defaultOption.prototype.deselect = function() {
	if(this.content) {
		this.render();
	}
	this.content.className = "";
};

/**
 *	@desc: set value of option
 *	@param: value - (string) value
 *	@param: text - (string) text
 *	@param: css - (string) css style string
 *	@type: public
 *	@topic: 4
 **/
dhtmlXCombo_2_defaultOption.prototype.setValue = function(attr){
	this.value = attr.value || "";
	this.text = attr.text || "";
	this.css = attr.css || "";
	this.content = null;
};

/**
 *	@desc: render option
 *	@type: private
 *	@topic: 4
 **/
dhtmlXCombo_2_defaultOption.prototype.render = function() {
	if(!this.content) {
		this.content = document.createElement("DIV");
        this.content._self = this;
        this.content.style.cssText = 'width:100%; overflow:hidden; "+this.css+"';
        if (_isOpera || _isKHTML ) this.content.style.padding = "2px 0px 2px 0px";
        this.content.innerHTML = this.text;
        this._ctext = _isIE ? this.content.innerText : this.content.textContent;
	}
	return this.content;
};

/**
 *     @desc: return option data
 *     @type: public
 *     @return: array of data related to option
 *     @topic: 4
 */
dhtmlXCombo_2_defaultOption.prototype.data = function(){
	if (this.content)
		return [this.value,this._ctext ? this._ctext : this.text];
};

dhtmlXCombo_2_defaultOption.prototype.DrawHeader = function(self, name, width, tab) {
	var z = document.createElement("DIV");
    z.style.width = width+"px";
	z.style.height = "18px";
    z.className = 'dhx_combo_box';
    z._self = self;
	self.DOMelem = z;
    this._DrawHeaderInput(self, name, width, tab);
    self.DOMParent.appendChild(self.DOMelem);
};

dhtmlXCombo_2_defaultOption.prototype._DrawHeaderInput = function(self, name, width,tab) {
	var z = document.createElement('input');
	z.className = 'dhx_combo_input';
	 z.setAttribute("autocomplete","off"); 
	z.type = 'text';
	z.id = "linename";
	z.name = "linename";
	z.style.border = '0px';	//设置边框为0，避免与父控件重叠
	if(tab) z.tabIndex = tab;
	z.style.width = (width) + 'px';
	z.style.height = '15px';
	self.DOMelem.appendChild(z);
	self.DOMelem_input = z;
	z = document.createElement('input');
	  z.type = 'hidden';
	z.name = name;
	z.style.height = "15px";
	self.DOMelem.appendChild(z);
	self.DOMelem_hidden_input = z;
	z = document.createElement('input');
	z.type = 'hidden';
	z.style.height = "15px";
	z.name = name + "_new_value";
	z.value = "true";
	self.DOMelem.appendChild(z);
	self.DOMelem_hidden_input2 = z;
};

dhtmlXCombo_2_optionTypes['default'] = dhtmlXCombo_2_defaultOption;

/**
 * 注册事件
 * @return
 */
dhtmlXCombo_2.prototype.dhx_Event = function() {
	this.dhx_SeverCatcherPath = "";
	this.attachEvent = function(original, catcher, CallObj) {
		CallObj = CallObj || this;
		original = 'ev_' + original;
		if((!this[original]) || (!this[original].addEvent)) {
			var z = new this.eventCatcher(CallObj);
			z.addEvent(this[original]);
			this[original] = z;
		}
		return (original + ':' + this[original].addEvent(catcher));   //return ID (event name & event ID)
	};
	this.callEvent = function(name,arg0) {
		if (this["ev_"+name]) return this["ev_"+name].apply(this,arg0);
		return true;
	};
	this.checkEvent = function(name) {
		if (this["ev_"+name]) return true;
		return false;
	};
	this.eventCatcher = function(obj) {
		var dhx_catch = new Array();
		var m_obj = obj;
		var func_server = function(catcher,rpc) {
			catcher = catcher.split(":");
			var postVar = "";
			var postVar2 = "";
			var target = catcher[1];
			if(catcher[1] == "rpc") {
				postVar = '<?xml version="1.0"?><methodCall><methodName>' + catcher[2] + '</methodName><params>';
				postVar2 = "</params></methodCall>";
				target = rpc;
			}
			var z = function() {};
			return z;
		};
		var z = function() {
			if(dhx_catch) {
				var res = true;
			}
			for(var i = 0; i < dhx_catch.length; i++) {
				if(dhx_catch[i] != null) {
					var zr = dhx_catch[i].apply(m_obj, arguments);
					res = res && zr;
				}
			}
			return res;
		};
		z.addEvent = function(ev) {
			if(typeof(ev) != "function")
				if (ev && ev.indexOf && ev.indexOf("server:") == 0)
					ev = new func_server(ev,m_obj.rpcServer);
				else
					ev = eval(ev);
			if (ev)
				return dhx_catch.push( ev ) - 1;
			return false;
		};
		z.removeEvent = function(id) {
			dhx_catch[id] = null;
		};
		return z;
	};
	this.detachEvent = function(id) {
		if(id != false) {
			var list = id.split(':');            //get EventName and ID
			this[list[0]].removeEvent( list[1]);   //remove event
		}
	};
};
//(c)dhtmlx ltd. www.dhtmlx.com