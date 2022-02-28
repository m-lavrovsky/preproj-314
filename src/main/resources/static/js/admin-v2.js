async function reloadUsersTable() {
    const outputDIV = document.querySelector('.all-user-table');
    let output = "<caption>&nbsp;Все пользователи</caption>\n"+
        "<thead>\n" +
        "        <tr>\n" +
        "            <th width=\"35 px\">#</th>\n" +
        "            <th width=\"35 px\">id</th>\n" +
        "            <th>Имя</th>\n" +
        "            <th>Фамилия</th>\n" +
        "            <th>Возраст</th>\n" +
        "            <th>Username</th>\n" +
        "            <th>Права</th>\n" +
        "            <th>Править</th>\n" +
        "            <th>Удалить</th>\n" +
        "        </tr>\n" +
        "        </thead><tbody>";

    const response = await fetch('/admin/users');
    const users = await response.json();

    let ctr = 1;
    users.forEach(function(user){
        let uid = user['id'];
        output += '<tr><td>'+ctr+'</td>' +
                '<td id="js-allusers-id-'+uid+'">'+uid+'</td>' +
                '<td id="js-allusers-name-'+uid+'">'+user['name']+'</td>' +
                '<td id="js-allusers-lastname-'+uid+'">'+user['lastname']+'</td>' +
                '<td id="js-allusers-age-'+uid+'">'+user['age']+'</td>' +
                '<td id="js-allusers-username-'+uid+'">'+user['username']+'</td><td id="js-allusers-roles-'+uid+'">';
        for (role of user.roles) {
            output += role.nicename + '<br>';
        }
        output += '</td><td><button type="button" onclick="getEditModalForm(' + user['id']+')" class="btn btn-info">Редактировать</button></td>\n' +
            '                <td><button type="button" onclick="getDelModalForm(' + user['id']+')" class="btn btn-danger">Удалить</button></td></tr>'
        ctr++;
    })
    output += "</tbody>";
    outputDIV.innerHTML = output;

    loadCurrentUserInfo();
}

async function loadCurrentUserInfo() {
    const outputDIV = document.querySelector('.my-user-table');
    const headerUserId = document.getElementById('js-header-id').textContent;
    const headerUsername = document.getElementById('js-header-username');
    const headerRoles = document.getElementById('js-header-roles');
    const response = await fetch('/admin/users/'+headerUserId);
    const myUser = await response.json();

    headerUsername.textContent = myUser.username;

    let output = "<caption>&nbsp;Ваш пользователь</caption>\n"+
        "<thead>\n" +
        "        <tr>\n" +
        "            <th width=\"35 px\">id</th>\n" +
        "            <th>Имя</th>\n" +
        "            <th>Фамилия</th>\n" +
        "            <th>Возраст</th>\n" +
        "            <th>Username</th>\n" +
        "            <th>Права</th>\n" +
        "        </tr>\n" +
        "        </thead><tbody>";

    let uid = myUser['id'];
    output += '<tr><td id="js-myuser-id-'+uid+'">'+uid+'</td>' +
        '<td id="js-myuser-name-'+uid+'">'+myUser['name']+'</td>' +
        '<td id="js-myuser-lastname-'+uid+'">'+myUser['lastname']+'</td>' +
        '<td id="js-myuser-age-'+uid+'">'+myUser['age']+'</td>' +
        '<td id="js-myuser-username-'+uid+'">'+myUser['username']+'</td><td id="js-myuser-roles-'+uid+'">';
    let ctr = 0;
    let myRoles = "";
    for (role of myUser.roles) {
        output += role.nicename + ' ';
        if (ctr > 0) {
            myRoles += ', ';
        }
        myRoles += role.nicename;
        ctr++;
    }
    output += '</td></tr></tbody>';

    outputDIV.innerHTML = output;
    headerRoles.textContent = myRoles;
}

reloadUsersTable();

var editModal = document.getElementById("editUserModal");
var deleteModal = document.getElementById("deleteUserModal");
var editCloseBtn = document.getElementsByClassName("close_edit_modal")[0];
var editCloseBtn2 = document.getElementsByClassName("close_edit_modal")[1];
var delCloseBtn = document.getElementsByClassName("close_delete_modal")[0];
var delCloseBtn2 = document.getElementsByClassName("close_delete_modal")[1];

function getEditModalForm(id) {
    $(document).ready(function(){
        $("#editUserModal").modal('show');
    });
    editModal.style.display = "block";

    // поля на модалке редактирования
    var editIdField = document.getElementById("edit-id");
    var editNameField = document.getElementById("edit-name");
    var editLastnameField = document.getElementById("edit-lastname");
    var editAgeField = document.getElementById("edit-age");
    var editUsernameField = document.getElementById("edit-username");
    var editRolesField = document.getElementById("edit-roles");

    editIdField.value = document.getElementById("js-allusers-id-"+id).innerText;
    editNameField.value = document.getElementById("js-allusers-name-"+id).innerText;
    editLastnameField.value = document.getElementById("js-allusers-lastname-"+id).innerText;
    editAgeField.value = document.getElementById("js-allusers-age-"+id).innerText;
    editUsernameField.value = document.getElementById("js-allusers-username-"+id).innerText;

    let roles = document.getElementById("js-allusers-roles-"+id).innerText;
    editRolesField.options[0].selected = (roles.indexOf('админ') != -1);
    editRolesField.options[1].selected = (roles.indexOf('пользователь') != -1);
}

async function sendEditUserData() {
    console.log('user data changes initiated');
    var id = document.getElementById("edit-id").value;
    var editRolesField = document.getElementById("edit-roles");
    let roleString = "";
    if (editRolesField.options[0].selected) {roleString += editRolesField.options[0].value};
    if (editRolesField.options[1].selected) {roleString += " " + editRolesField.options[1].value};
    if (id != null) {
        let user = {
            "id": id,
            "name": document.getElementById("edit-name").value,
            "lastname": document.getElementById("edit-lastname").value,
            "age": document.getElementById("edit-age").value,
            "password": document.getElementById("edit-password").value,
            "username": document.getElementById("edit-username").value,
            "roleStringJS": roleString
        };
        let response = await fetch('/admin/users', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            body: JSON.stringify(user)
        });
        let result = await response.text();
        console.log(result);
    }

    $(document).ready(function(){
        $("#editUserModal").modal('hide').fadeOut();
    });
    editModal.style.display = "none";
    window.reloadUsersTable();
}

function getDelModalForm(id) {
    $(document).ready(function(){
        $("#deleteUserModal").modal('show');
    });
    deleteModal.style.display = "block";
    // поля на модалке редактирования
    var deleteIdField = document.getElementById("delete-id");
    var deleteNameField = document.getElementById("delete-name");
    var deleteLastnameField = document.getElementById("delete-lastname");
    var deleteAgeField = document.getElementById("delete-age");
    var deleteUsernameField = document.getElementById("delete-username");
    var deleteRolesField = document.getElementById("delete-roles");

    deleteIdField.value = document.getElementById("js-allusers-id-"+id).innerText;
    deleteNameField.value = document.getElementById("js-allusers-name-"+id).innerText;
    deleteLastnameField.value = document.getElementById("js-allusers-lastname-"+id).innerText;
    deleteAgeField.value = document.getElementById("js-allusers-age-"+id).innerText;
    deleteUsernameField.value = document.getElementById("js-allusers-username-"+id).innerText;
    let roles = document.getElementById("js-allusers-roles-"+id).innerText.split(' ');
    deleteRolesField.innerHTML = '';
    for (role of roles) {
        deleteRolesField.innerHTML += '<option>'+role+'</option>';
    }
}

async function sendDeleteUserData() {
    console.log('deletetion initiated');
    var deleteIdField = document.getElementById("delete-id");
    id = deleteIdField.value;
    if (id != null) {
        let response = await fetch('/admin/users/'+id, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'text/plain;charset=utf-8'
            }
        });
        let result = await response.text();
        console.log(result);
    }

    $(document).ready(function(){
        $("#deleteUserModal").modal('hide').fadeOut();
    });
    deleteModal.style.display = "none";
    window.reloadUsersTable();
}

async function sendNewUserData() {
    console.log('new user adding initiated');
    //var id = document.getElementById("new-id").value;
    var newRolesField = document.getElementById("new-roles");
    let roleString = "";
    if (newRolesField.options[0].selected) {roleString += newRolesField.options[0].value};
    if (newRolesField.options[1].selected) {roleString += " " + newRolesField.options[1].value};
    let user = {
        "name": document.getElementById("new-name").value,
        "lastname": document.getElementById("new-lastname").value,
        "age": document.getElementById("new-age").value,
        "password": document.getElementById("new-password").value,
        "passwordConfirm": document.getElementById("new-passwordconfirm").value,
        "username": document.getElementById("new-username").value,
        "roleStringJS": roleString
    };
    console.log(document.getElementById("new-password").value);
    console.log(document.getElementById("new-passwordconfirm").value);
    let response = await fetch('/admin/users', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify(user)
    });
    let result = await response.text();
    console.log(result);

    window.reloadUsersTable();
}

editCloseBtn.onclick = function () {
    $(document).ready(function(){
        $("#editUserModal").modal('hide').fadeOut();
    });
    editModal.style.display = "none";
}

delCloseBtn.onclick = function () {
    $(document).ready(function(){
        $("#deleteUserModal").modal('hide').fadeOut();
    });
    deleteModal.style.display = "none";
}

editCloseBtn2.onclick = function () {
    $(document).ready(function(){
        $("#editUserModal").modal('hide').fadeOut();
    });
    editModal.style.display = "none";
}

delCloseBtn2.onclick = function () {
    $(document).ready(function(){
        $("#deleteUserModal").modal('hide').fadeOut();
    });
    deleteModal.style.display = "none";
}

//блокировка клика вне окна, включена, если закомментить
/*window.onclick = function (event) {
    if (event.target == editModal) {
        $(document).ready(function(){
            $("#editUserModal").modal('hide').fadeOut();
        });
        editModal.style.display = "none";
    }
    if (event.target == deleteModal) {
        $(document).ready(function(){
            $("#deleteUserModal").modal('hide').fadeOut();
        });
        deleteModal.style.display = "none";
    }
}*/