
$(document).ready(function () {

    $("a[name='linkRemoveDetail']").each(function (index) {
        $(this).click(function () {
            removeDetailSectionByIndex(index);
        });

    });
});



function addNextDetailSection(){
    allDivDetails = $("[id^='divDetails']")
    divDetailsCount = allDivDetails.length;



htmlDetailSection=`  
  <div className="form-inline" id="divDetails${divDetailsCount}">
    <input type="hidden" name="detailIDs" value="0"/>
    <label className="m-3">Name:</label>
    <input type="text" className="form-control w-25" name="detailNames" maxLength="255"/>
    <label className="m-3">Value:</label>
    <input type="text" className="form-control w-25" name="detailValues" maxLength="255"/>
</div>` ;

$("#divProductDetails").append(htmlDetailSection);
    previousDivDetailSection = allDivDetails.last();
    previousDivDetailId = previousDivDetailSection.attr("id");
    htmlLinkRemove=`
            <a class="btn fas fa-times-circle fa-2x icon-dark "
            href="javascript:removeDetailSectionById('${previousDivDetailId}')"
             title="Remove this detail"></a>` ;



    previousDivDetailSection.append(htmlLinkRemove);
    $("input[name='detailNames']").last().focus();

}
function removeDetailSectionById(id){
    $("#"+ id).remove();
}

function removeDetailSectionByIndex(index){
    $("#divDetails" + index).remove();
}