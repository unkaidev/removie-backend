function addBackdropField() {
    var container = document.getElementById('backdropContainer');
    if (!container) {
        container = document.createElement('div');
        container.id = 'backdropContainer';
        document.body.appendChild(container);
    }

    var newInputGroup = document.createElement('div');
    newInputGroup.className = 'input-group mb-3';

    var newInput = document.createElement('input');
    var index = document.querySelectorAll('.form-control').length + 1;
    newInput.type = 'text';
    newInput.className = 'form-control';
    newInput.name = 'backdrops';
    newInput.id = 'backdropInput_' + index;
    newInput.required = true;

    var newLabel = document.createElement('label');
    newLabel.appendChild(newInput);

    var refreshButton = document.createElement('button');
    refreshButton.type = 'button';
    refreshButton.className = 'btn btn-primary';
    refreshButton.onclick = function () {
        refreshImage(newInput.value, 'addBackdrop');
    };
    refreshButton.textContent = 'Preview';

    newLabel.appendChild(refreshButton);

    var newButton = document.createElement('button');
    newButton.type = 'button';
    newButton.className = 'btn btn-danger';
    newButton.onclick = function () {
        removeBackdropField(index);
    };

    var newIcon = document.createElement('i');
    newIcon.className = 'fa fa-times';
    newButton.appendChild(newIcon);

    newLabel.appendChild(newButton);
    newInputGroup.appendChild(newLabel);

    container.appendChild(newInputGroup);
}


function refreshImage(input, imageId) {
    var inputValue = input;
    var imageElement = document.getElementById(imageId);
    if (imageElement) {
        imageElement.src = inputValue;
    } else {
        console.error('ID not found ' + imageId);
    }
}


function removeBackdropField(index) {
    var inputToRemove = document.getElementById('backdropInput_' + index);

    if (inputToRemove) {
        var parentDiv = inputToRemove.closest('.input-group.mb-3'); // Assuming the parent div has this class
        if (parentDiv) {
            parentDiv.remove();
        } else {
            console.error('Không tìm thấy phần tử div cha với ID backdropInput_' + index);
        }

        var imageId = 'addBackdrop';
        var imageElement = document.getElementById(imageId);
        if (imageElement) {
            imageElement.src = '';
        }
    } else {
        console.error('Không tìm thấy phần tử với ID backdropInput_' + index);
    }
}

function refreshImagePoster() {
    var posterImage = document.getElementById('posterImage');

    var posterUrl = document.getElementById('poster').value;

    if (posterImage === null) {
        var divElement = document.createElement("div");

        var imageElement = document.createElement("img");

        imageElement.src = posterUrl;
        imageElement.className = 'poster my-3';

        divElement.appendChild(imageElement);

        var imageContainer = document.getElementById("imageContainer");
        imageContainer.appendChild(divElement);
    } else {
        posterImage.src = posterUrl;
    }
}
