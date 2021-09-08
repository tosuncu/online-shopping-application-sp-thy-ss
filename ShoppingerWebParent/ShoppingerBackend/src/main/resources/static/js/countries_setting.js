var buttonLoad;
var dropdownCountries;
var buttonAddCountry;
var buttonUpdateCountry;
var buttonDeleteCountry;
var labelCountryName;
var fieldCountryName;
var fieldCountryCode;


$(document).ready(function () {
    buttonLoad = $("#buttonLoadCountries");
    dropdownCountries = $("#dropdownCountries");
    buttonAddCountry = $("#buttonAddCountry")
    buttonUpdateCountry = $("#buttonUpdateCountry")
    buttonDeleteCountry = $("#buttonDeleteCountry")
    labelCountryName = $("#labelCountryName")
    fieldCountryName = $("#fieldCountryName")
    fieldCountryCode = $("#fieldCountryCode")

    buttonLoad.click(function () {
        loadCountries();
    });

    dropdownCountries.on("change", function () {
        changeFormStateToSelectedCountry();
    });

    buttonAddCountry.click(function () {
        if (buttonAddCountry.val() == "Add") {
            addCountry();
        } else {
            changeFormStateToNewCountry();
        }

        //changeFormStateToNew();
    });

    buttonUpdateCountry.click(function () {
        updateCountry();
    });
    buttonDeleteCountry.click(function () {
        deleteCountry();
    });
});

function deleteCountry() {
    optionValue = dropdownCountries.val();
    countryId = optionValue.split("-")[0];

    url = contextPath + "countries/delete/" + countryId;

    $.ajax({
        type: 'DELETE',
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        }
    }).done(function () {
        showToastMessage("The country has been deleted.")
    }).fail(function () {
        showToastMessage("ERROR: Could not connect to server or server encountered with an error.")
    });
}

function updateCountry() {

    if (!validateFormCountry()) return;
    url = contextPath + "countries/save";

    countryName = fieldCountryName.val();
    countryCode = fieldCountryCode.val();
    countryId = dropdownCountries.val().split("-")[0];

    jsonData = {id: countryId, name: countryName, code: countryCode};
    data = JSON.stringify(jsonData);


    $.ajax({
        type: 'post',
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: data,
        contentType: 'application/json'
    }).done(function (countryId) {
        $("#dropdownCountries option:selected").val(countryId + "-" + countryCode);
        $("#dropdownCountries option:selected").text(countryName);
        showToastMessage("The new country has been updated.");

        changeFormStateToNewCountry();
    }).fail(function () {
        showToastMessage("ERROR: Could not connect to server or server encountered an error");
    });
}

function validateFormCountry(){
    formCountry = document.getElementById("formCountry")
    if (!formCountry.checkValidity()) {
        formCountry.reportValidity();
        return false;
    }
    return true;
}
function addCountry() {

    if (!validateFormCountry()) return;

    url = contextPath + "countries/save";

    countryName = fieldCountryName.val();
    countryCode = fieldCountryCode.val();

    jsonData = {name: countryName, code: countryCode};
    data = JSON.stringify(jsonData);


    $.ajax({
        type: 'post',
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: data,
        contentType: 'application/json'
    }).done(function (countryId) {
        selectNewlyAddedCountry(countryId, countryCode, countryName);
        showToastMessage("The new country has been added.");
    }).fail(function () {
        showToastMessage("ERROR: Could not connect to server or server encountered an error")
    });
}

function selectNewlyAddedCountry(countryId, countryCode, countryName) {
    optionValue = countryId + " - " + countryCode;
    $("<option>").val(optionValue).text(countryName).appendTo(dropdownCountries);

    $("#dropdownCountries option[value='" + optionValue + "']").prop("selected", true);

    fieldCountryCode.val("");
    fieldCountryName.val("").focus();
}

function changeFormStateToNewCountry() {
    buttonAddCountry.val("Add");
    labelCountryName.text("Country Name:");

    buttonUpdateCountry.prop("disabled", true);
    buttonDeleteCountry.prop("disabled", true);

    fieldCountryCode.val("");
    fieldCountryName.val("").focus();

}

function changeFormStateToSelectedCountry() {
    buttonAddCountry.prop("value", "New");
    buttonUpdateCountry.prop("disabled", false);
    buttonDeleteCountry.prop("disabled", false);

    labelCountryName.text("Selected Country");

    selectedCountryName = $("#dropdownCountries option:selected").text();
    fieldCountryName.val(selectedCountryName);

    countryCode = dropdownCountries.val().split("-")[1];
    fieldCountryCode.val(countryCode);

}

function loadCountries() {
    url = contextPath + "countries/list";
    $.get(url, function (responseJSON) {
        dropdownCountries.empty();

        $.each(responseJSON, function (index, country) {
            optionValue = country.id + "-" + country.code;
            $("<option>").val(optionValue).text(country.name).appendTo(dropdownCountries);
        });
    }).done(function () {
        buttonLoad.val("Refresh Country List");
        showToastMessage("All countries have been loaded.")
    }).fail(function () {
        showToastMessage("ERROR: Could not connect to server or server encountered with an error.")
    });
}

function showToastMessage(message) {
    $("#toastMessage").text(message);
    $(".toast").toast('show')
}