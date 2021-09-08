var buttonLoadStates;
var dropdownCountriesForStates;
var dropdownStates;
var buttonAddState;
var buttonUpdateState;
var buttonDeleteState;
var labelStateName;
var fieldStateName


$(document).ready(function () {
    buttonLoadStates = $("#buttonLoadCountriesForStates");
    dropdownCountriesForStates = $("#dropdownCountriesForStates");
    dropdownStates = $("#dropdownStates")
    buttonAddState = $("#buttonAddState")
    buttonUpdateState = $("#buttonUpdateState")
    buttonDeleteState = $("#buttonDeleteState")
    labelStateName = $("#labelStateName")
    fieldStateName = $("#fieldStateName")

    buttonLoadStates.click(function () {
        loadCountriesForStates();
    });

    dropdownCountriesForStates.on("change", function () {
        loadStatesForCountry();
    });
    dropdownStates.on("change",function (){
        changeFormStateToSelectedState();
    });

    buttonAddState.click(function () {
        if (buttonAddState.val() == "Add") {
            addState();
        } else {
            changeFormStateToNew();
        }
    });

    buttonUpdateState.click(function () {
        updateState();
    });
    buttonDeleteState.click(function () {
        deleteState();
    });
});

function deleteState() {
    stateId = dropdownStates.val();

    url = contextPath + "states/delete/" + stateId;

    $.ajax({
        type: 'DELETE',
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        }
    }).done(function () {
        showToastMessage("The state has been deleted.")
    }).fail(function () {
        showToastMessage("ERROR: Could not connect to server or server encountered with an error.")
    });
}

function updateState() {
    if (!validateFormState()) return;
    url = contextPath + "states/save";
    stateId = dropdownStates.val();
    stateName = fieldStateName.val();

    selectedCountry = $("#dropdownCountriesForStates option:selected");
    countryId = selectedCountry.val();
    countryName = selectedCountry.text();

    jsonData = {id: stateId, name: stateName, country: {id: countryId, name: countryName}};
    data = JSON.stringify(jsonData);

    $.ajax({
        type: 'post',
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: data,
        contentType: 'application/json'
    }).done(function (stateId) {
        $("#dropdownStates option:selected").text(stateName);
        showToastMessage("The state has been updated.");
        changeFormStateToNew();
    }).fail(function () {
        showToastMessage("ERROR: Could not connect to server or server encountered an error");
    });
}

function validateFormState(){
    formState = document.getElementById("formState");
    if (!formState.checkValidity()) {
        formState.reportValidity();
        return false;
    }
    return true;
}

function addState() {
    if (!validateFormState()) return;
    url = contextPath + "states/save";

    stateName = fieldStateName.val();

    selectedCountry = $("#dropdownCountriesForStates option:selected");
    countryId = selectedCountry.val();
    countryName = selectedCountry.text();

    jsonData = {name: stateName, country: {id: countryId, name: countryName}};
    data = JSON.stringify(jsonData);

    $.ajax({
        type: 'post',
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: data,
        contentType: 'application/json'
    }).done(function (stateId) {
        selectNewlyAddedState(stateId, stateName);
        showToastMessage("The new state has been added.");
    }).fail(function () {
        showToastMessage("ERROR: Could not connect to server or server encountered an error")
    });
}

function selectNewlyAddedState(stateId, stateName) {
    $("<option>").val(stateId).text(stateName).appendTo(dropdownStates);

    $("#dropdownStates option[value='" + stateId + "']").prop("selected", true);


    fieldStateName.val("").focus();
}

function changeFormStateToNew() {
    buttonAddState.val("Add");
    labelStateName.text("State/Province Name:");

    buttonUpdateState.prop("disabled", true);
    buttonDeleteState.prop("disabled", true);

    fieldStateName.val("").focus();

}

function changeFormStateToSelectedState() {
    buttonAddState.prop("value", "New");
    buttonUpdateState.prop("disabled", false);
    buttonDeleteState.prop("disabled", false);

    labelStateName.text("Selected State/Province Name:");

    selectedStateName = $("#dropdownStates option:selected").text();
    fieldStateName.val(selectedStateName);

}


function loadStatesForCountry() {
    selectedCountry = $("#dropdownCountriesForStates option:selected");
    countryId = selectedCountry.val();
    url = contextPath + "states/list_by_country/" + countryId;

    $.get(url, function (responseJSON) {
        dropdownStates.empty();
        $.each(responseJSON, function (index, state) {
            $("<option>").val(state.id).text(state.name).appendTo(dropdownStates);
        });
    }).done(function () {
        changeFormStateToNew();
        showToastMessage("All states have been kıaded for country " + selectedCountry.text());
    }).fail(function () {
        showToastMessage("ERROR: Could not connect to server or server encountered an error")
    });
}

function loadCountriesForStates() {
    url = contextPath + "countries/list";
    $.get(url, function (responseJSON) {
        dropdownCountriesForStates.empty();

        $.each(responseJSON, function (index, country) {
            $("<option>").val(country.id).text(country.name).appendTo(dropdownCountriesForStates);
        });
    }).done(function () {
        buttonLoadStates.val("Refresh Country List");
        showToastMessage("All countries have been loaded");
    }).fail(function () {
        showToastMessage("ERROR: Could not connect to server or server encountered with problem");
    });
}

function showToastMessage(message) {
    $("#toastMessage").text(message);
    $(".toast").toast('show')
}