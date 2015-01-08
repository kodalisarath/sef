<script src="js/business_rules_management.js"></script>
<script>
    $(document).ready(function () {

        $(document).on("click", ".addenum", function () {
            $("#appendenumerated").append($("#enumerated").html());
        });
        
        $(document).on("click", ".addenum-list", function () {
            $("#appendenumerated-list").append($("#enumerated-list").html());
        });
    });
</script>
<style>
    #range-brm {
        display: none;
    }
    #exclusion-brm {
        display: none;
    }
    #notrange-brm {
        display: none;
    }
    #start-brm {
        display: none;
    }
    #doesntstart-brm {
        display: none;
    }
    #end-brm {
        display: none;
    }
    #doesntend-brm {
        display: none;
    }
    #match-brm {
        display: none;
    }
    #notmatch-brm {
        display: none;
    }
    #equal-brm {
        display: none;
    }
    #notequal-brm {
        display: none;
    }
    #greaterthan-brm {
        display: none;
    }
    #greaterthanequal-brm {
        display: none;
    }
    #lessthan-brm {
        display: none;
    }
    #lessthanequal-brm {
        display: none;
    }
}
</style>
<div id="add-offer" style="float:left; width:100%;">
<button value="Add Rule" id="rule-btn">Add Rule</button>
</div>
<div id="content-rule">

    <form id="create-rule">

        <div class="form-row">
            <div class="label" style="float:left; width:160px;">Rule Name</div><em>*</em>
        </div>
        <div class="input-field">
            <input class="input-field-style mandatory constrained offerName" maxlength="20" name="name" id="group-name">
        </div>

        <div class="form-row">
            <div class="label">Logic Gate</div><em>*</em>
            <div id="dd5">
                <select name="gate">
                    <option value="select">Select</option>
                    <option value="AND">All of the following</option>
                    <option value="OR">Any of the following</option>
                    <option value="NAND">None of the Following</option>
                    <option value="NOR">Not any of the following</option>
                </select>
            </div>
        </div>

        <div class="price" style="margin-top:10px; width:100%;float:left;">
            <div class="history4">Rule Unit</div>
            <div id="panel4">

                <div class="form-row">
                    <div class="label" style="float:left; width:160px;">RuleUnit Name</div><em>*</em>
                </div>
                <div class="input-field">
                    <input class="input-field-style mandatory constrained offerName" maxlength="20" name="unitName" id="group-name">
                </div>

                <div class="form-row">
                    <div class="label">Base Object</div><em>*</em>
                    <div id="dd5">
                        <select name="baseObject">
                            <option value="select">Select</option>
                            <option value="RequestContext">Request Context</option>
                            <option value="SubscriberProfile">Subscriber Profile</option>
                        </select>
                    </div>
                </div>

                <div class="form-row">
                    <div class="label">Condition</div><em>*</em>
                    <div id="dd5">
                        <select name="condition">
                            <option value="select">Select</option>
                            <option value="ENUMERATED">IN_LIST_OF</option>
                            <option value="EXCLUSION_LIST">EXCLUSION_LIST</option>
                            <option value="RANGE">RANGE</option>
                            <option value="NOT_IN_RANGE">NOT_IN_RANGE</option>
                            <option value="STARTS_WITH">STARTS_WITH</option>
                            <option value="DOESNT_START_WITH">DOESNT_START_WITH</option>
                            <option value="ENDS_WITH">ENDS_WITH</option>
                            <option value="DOESNT_END_WITH">DOESNT_END_WITH</option>
                            <option value="CONTAINS">CONTAINS</option>
                            <option value="DOESNT_CONTAIN">DOESNT_CONTAIN</option>
                            <option value="MATCHES">MATCHES</option>
                            <option value="NOT_MATCHES">NOT_MATCHES</option>
                            <option value="EQUALS">EQUALS</option>
                            <option value="NOT_EQUALS">NOT_EQUALS</option>
                            <option value="GREATER_THAN">GREATER_THAN</option>
                            <option value="GREATER_THAN_OR_EQUALS">GREATER_THAN_OR_EQUALS</option>
                            <option value="LESSER_THAN">LESSER_THAN</option>
                            <option value="LESSER_THAN_OR_EQUALS">LESSER_THAN_OR_EQUALS</option>
                        </select>
                    </div>
                </div>
                <div class="price" style="margin-top: 0px !important; width: 100%; float: left; ">
                    <div id="enum-brm" style="display:none; margin-bottom:5px;width: 94%; border:1px dashes #f6f6f6; border-radius:5px; background:#f3f3f3;padding:15px; float: left;">
                        <div id="enumerated">

                            <div class="form-row">
                                <div class="label" style="float: left; width: auto;">Enumerated Value &nbsp;&nbsp;
                                </div>
                                <em>*</em> 
                                <input class="input-field-style mandatory constrained offerName" name="enumeratedValue" id="group-name" style="width:135px;">
                            </div>

                        </div>
                        <div class="addenum" style="cursor: pointer; float: right; margin-top: -5px;">
                            <img src="images/add.png" />
                        </div>

                        <div id="appendenumerated"></div>
                    </div>
                </div>

                <div class="price" style="margin-top: 0px !important; width: 100%; float: left; ">
                    <div id="exclusion-brm" style="display:none; margin-bottom:5px;width: 94%; border:1px dashes #f6f6f6; border-radius:5px; background:#f3f3f3;padding:15px; float: left;">
                        <div id="enumerated-list">

                            <div class="form-row">
                                <div class="label" style="float: left; width: auto;">Exclusion List &nbsp;&nbsp;
                                </div>
                                <em>*</em> 
                                <input class="input-field-style mandatory constrained offerName" name="exclusionList" id="group-name" style="width:135px;">
                            </div>

                        </div>
                        <div class="addenum-list" style="cursor: pointer; float: right; margin-top: -5px;">
                            <img src="images/add.png" />
                        </div>

                        <div id="appendenumerated-list"></div>
                    </div>
                </div>

                <div class="price" style="margin-top: 0px !important; width: 100%; float: left; ">
                    <div id="range-brm" style="margin-bottom:5px; width: 94%;float: left;">
                        <div class="form-row">
                            <div class="label" style="float:left; width:160px;">UpperBound</div><em>*</em>
                        </div>
                        <div class="input-field">
                            <input class="input-field-style mandatory constrained " maxlength="20" name="upperBound">
                        </div>
                        <div class="form-row">
                            <div class="label" style="float:left; width:160px;">LowerBound</div><em>*</em>
                        </div>
                        <div class="input-field">
                            <input class="input-field-style mandatory constrained " maxlength="20" name="lowerBound">
                        </div>
                    </div>
                </div>

                <div class="price" style="margin-top: 0px !important; width: 100%; float: left; ">
                    <div id="start-brm" style="margin-bottom:5px; width: 94%;float: left;">
                        <div class="form-row">
                            <div class="label" style="float:left; width:160px;">Starts with Pattern</div><em>*</em>
                        </div>
                        <div class="input-field">
                            <input class="input-field-style mandatory constrained " maxlength="20" name="startWithPattern">
                        </div>
                    </div>
                </div>

                <div class="price" style="margin-top: 0px !important; width: 100%; float: left; ">
                    <div id="doesntstart-brm" style="margin-bottom:5px; width: 94%;float: left;">
                        <div class="form-row">
                            <div class="label" style="float:left; width:160px;">Doesnt Starts with Pattern</div><em>*</em>
                        </div>
                        <div class="input-field">
                            <input class="input-field-style mandatory constrained " maxlength="20" name="notStartWithPattern">
                        </div>
                    </div>
                </div>

                <div class="price" style="margin-top: 0px !important; width: 100%; float: left; ">
                    <div id="end-brm" style="margin-bottom:5px; width: 94%;float: left;">
                        <div class="form-row">
                            <div class="label" style="float:left; width:160px;">Ends with Pattern</div><em>*</em>
                        </div>
                        <div class="input-field">
                            <input class="input-field-style mandatory constrained " maxlength="20" name="endWithPattern">
                        </div>
                    </div>
                </div>

                <div class="price" style="margin-top: 0px !important; width: 100%; float: left; ">
                    <div id="doesntend-brm" style="margin-bottom:5px; width: 94%;float: left;">
                        <div class="form-row">
                            <div class="label" style="float:left; width:160px;">Doesnt Ends with Pattern</div><em>*</em>
                        </div>
                        <div class="input-field">
                            <input class="input-field-style mandatory constrained " maxlength="20" name="notEndWithPattern">
                        </div>
                    </div>
                </div>

                <div class="price" style="margin-top: 0px !important; width: 100%; float: left; ">
                    <div id="contain-brm" style="margin-bottom:5px; width: 94%;float: left;display:none;">
                        <div class="form-row">
                            <div class="label" style="float:left; width:160px;">Contains with Pattern</div><em>*</em>
                        </div>
                        <div class="input-field">
                            <input class="input-field-style mandatory constrained " maxlength="20" name="containWithPattern">
                        </div>
                    </div>
                </div>

                <div class="price" style="margin-top: 0px !important; width: 100%; float: left; ">
                    <div id="doesntcontain-brm" style="margin-bottom:5px; width: 94%;float: left;display:none;">
                        <div class="form-row">
                            <div class="label" style="float:left; width:160px;">Doesnt Contains with Pattern</div><em>*</em>
                        </div>
                        <div class="input-field">
                            <input class="input-field-style mandatory constrained " maxlength="20" name="notContainWithPattern">
                        </div>
                    </div>
                </div>

                <div class="price" style="margin-top: 0px !important; width: 100%; float: left; ">
                    <div id="match-brm" style="margin-bottom:5px; width: 94%;float: left;">
                        <div class="form-row">
                            <div class="label" style="float:left; width:160px;">Matches Pattern</div><em>*</em>
                        </div>
                        <div class="input-field">
                            <input class="input-field-style mandatory constrained " maxlength="20" name="matchesPattern">
                        </div>
                    </div>
                </div>

                <div class="price" style="margin-top: 0px !important; width: 100%; float: left; ">
                    <div id="notmatch-brm" style="margin-bottom:5px; width: 94%;float: left;">
                        <div class="form-row">
                            <div class="label" style="float:left; width:160px;">Not Matches Pattern</div><em>*</em>
                        </div>
                        <div class="input-field">
                            <input class="input-field-style mandatory constrained " maxlength="20" name="notMatchesPattern">
                        </div>
                    </div>
                </div>

                <div class="price" style="margin-top: 0px !important; width: 100%; float: left; ">
                    <div id="equal-brm" style="margin-bottom:5px; width: 94%;float: left;">
                        <div class="form-row">
                            <div class="label" style="float:left; width:160px;">Rule</div><em>*</em>
                        </div>
                        <div class="input-field">
                            <input class="input-field-style mandatory constrained " maxlength="20" name="ruleCondition">
                        </div>
                    </div>
                </div>


                <div class="price" style="margin-top: 0px !important; width: 100%; float: left; ">
                    <div id="greaterthan-brm" style="margin-bottom:5px; width: 94%;float: left;">
                        <div class="label">Rule Value</div><em>&nbsp;&nbsp;</em>
                        <input type="radio" name="conditionValue" value="number" />Number
                        <input onkeypress="return isNumberKey(event)" type="text" id="number" name="conditionValueNumber" min="1" maxlength="9" style="width:16px;height:15px;display:none;">
                        <input type="radio" name="conditionValue" value="date" />Date
                        <input type="text" attr-date="datepicker" name="conditionValueDate" min="" id="date" style="width: 130px;display:none;" placeholder="yyyy-mm-dd">
                    </div>
                </div>

                <div style="float:left;width:100%;">
                    <div class="form-row">
                        <div class="label" style="float:left; width:160px;">Rule Variable</div><em>*</em>
                    </div>
                    <div class="input-field">
                        <input class="input-field-style mandatory constrained offerName" maxlength="20" name="ruleVariable" id="group-name">
                    </div>
                </div>

                <div style="float:left;width:100%;">
                    <div class="form-row">
                        <div class="label" style="float:left; width:160px;">Schema Name</div><em>*</em>
                    </div>
                    <div class="input-field">
                        <input class="input-field-style mandatory constrained offerName" maxlength="20" name="schemaName" id="group-name">
                    </div>
                </div>

                <input type="button" value="Add" id="add-brm" />
            </div>
        </div>

        <div style="width: 40%; float: left; margin-top: 10px;">
            <div class="label" style="width: 100%; height: 30px;" id="groups-assigned-label">
                Available RuleUnits <em>*</em>
            </div>

            <div>
                <select id="nonassigned-fields-unit" class="total-kpi-fields select-kpi-fields-list" multiple="multiple" style="height:auto; min-height:220px;border:1px solid #ccc; font-size:10px; font-family:Tahoma, Geneva, sans-serif; float:left;width: 230px; background:#fff;" name="cdrHeaderField">


                </select>
            </div>
        </div>

        <div id="assign-deassign-resource" class="navigate">
            <div style="margin-left:40px !important;">
                <center>
                    <img border="0" src="images/right.png" id="img-right-unit" title="Move to Right">
                </center>

                <center>
                    <img border="0" src="images/left.png" id="img-left-unit" title="Move to Left">
                </center>
            </div>
        </div>

        <div class="label" style="width: 42%; height: 30px; float: right; margin-top: 10px;" id="groups-nonassigned-label">
            Choosen RuleUnits <em>*</em>
        </div>

        <div>
            <select id="assigned-fields-unit" class="total-kpi-fields select-kpi-fields-list" constraints="{" fieldLabel ":"Cdr Header Field ","mustSelect ":"true "}" multiple="multiple" style="height:auto; min-height:220px;border:1px solid #ccc; font-size:10px; font-family:Tahoma, Geneva, sans-serif; float:right;width: 230px; background:#fff;" name="cdrHeaderField">

            </select>

        </div>

        <div style="width: 40%; float: left; margin-top: 10px;">
            <div class="label" style="width: 100%; height: 30px;" id="groups-assigned-label">
                Available Rules <em>*</em>
            </div>

            <div>
                <select id="nonassigned-fields-rule" class="total-kpi-fields select-kpi-fields-list" multiple="multiple" style="height:auto; min-height:220px;border:1px solid #ccc; font-size:10px; font-family:Tahoma, Geneva, sans-serif; float:left;width: 230px; background:#fff;" name="cdrHeaderField">

                </select>
            </div>
        </div>

        <div id="assign-deassign-resource" class="navigate">
            <div style="margin-left:40px !important;">
                <center>
                    <img border="0" src="images/right.png" id="img-right-rule" title="Move to Right">
                </center>

                <center>
                    <img border="0" src="images/left.png" id="img-left-rule" title="Move to Left">
                </center>
            </div>
        </div>
        <div class="label" style="width: 42%; height: 30px; float: right; margin-top: 10px;" id="groups-nonassigned-label">
            Choosen Rules <em>*</em>
        </div>

        <div>
            <select id="assigned-fields-rule" class="total-kpi-fields select-kpi-fields-list" constraints="{" fieldLabel ":"Cdr Header Field ","mustSelect ":"true "}" multiple="multiple" style="height:auto; min-height:220px;border:1px solid #ccc; font-size:10px; font-family:Tahoma, Geneva, sans-serif; float:right;width: 230px; background:#fff;" name="cdrHeaderField">


            </select>

        </div>
		
		<div style="float:left; margin-left:365px; margin-top:15px;">
			<button value="Add Offer" id="save-btn-brm" class="btn" type="button">Save</button>
			<button value="Add Offer" id="update-btn-brm" class="btn" type="button">Update</button>
			<button value="Add Offer" id="cancel-btn-brm" class="btn" type="button">Cancel</button>
		</div>
    </form>
    
</div>