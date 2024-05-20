document.addEventListener('DOMContentLoaded', function() {
    const addNewCastButton = document.getElementById('addNewCastButton');
    const saveNewCastButton = document.getElementById('saveNewCastButton');
    const addNewCastModal = document.getElementById('addNewCastModal');

    saveNewCastButton.addEventListener('click', function() {
        var name = document.getElementById('castName').value;
        var knownForDepartment = document.getElementById('castKnownForDepartment').value;
        var character = document.getElementById('castCharacter').value;
        var profile = document.getElementById('castProfile').value;

        if (!name || !knownForDepartment || !character || !profile) {
            alert('Please fill in all fields.');
            return;
        }

        var newCast = {
            name: name,
            known_for_department: knownForDepartment,
            character: character,
            profile: profile
        };

        var xhr = new XMLHttpRequest();
        xhr.open("POST", "/api/v1/credit/cast", true);
        xhr.setRequestHeader("Content-Type", "application/json");
        xhr.onreadystatechange = function() {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status === 201) {
                    console.log("Cast saved successfully!");
                    addNewCastModal.style.display = 'none';
                    location.reload();
                } else {
                    console.error("Error saving cast: " + xhr.responseText);
                }
            }
        };
        xhr.send(JSON.stringify(newCast));
    });


    addNewCastButton.addEventListener('click', function(event) {
        event.preventDefault();

        addNewCastModal.style.display = 'block';
    });

    const addNewCrewButton = document.getElementById('addNewCrewButton');
    const saveNewCrewButton = document.getElementById('saveNewCrewButton');
    const addNewCrewModal = document.getElementById('addNewCrewModal');

    saveNewCrewButton.addEventListener('click', function() {
        var name = document.getElementById('crewName').value;
        var department = document.getElementById('crewDepartment').value;

        if (!name || !department) {
            alert('Please fill in all fields.');
            return;
        }

        var newCrew = {
            name: name,
            known_for_department: department
        };

        var xhr = new XMLHttpRequest();
        xhr.open("POST", "/api/v1/credit/crew", true);
        xhr.setRequestHeader("Content-Type", "application/json");
        xhr.onreadystatechange = function() {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status === 201) {
                    console.log("Crew saved successfully!");
                    addNewCrewModal.style.display = 'none';
                    location.reload();
                } else {
                    console.error("Error saving crew: " + xhr.responseText);
                }
            }
        };
        xhr.send(JSON.stringify(newCrew));
    });

    addNewCrewButton.addEventListener('click', function(event) {
        event.preventDefault();

        addNewCrewModal.style.display = 'block';
    });
});
