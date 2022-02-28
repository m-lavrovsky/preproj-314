loadCurrentUserInfo();

async function loadCurrentUserInfo() {
    const outputDIV = document.querySelector('.my-user-table');
    const headerUsername = document.getElementById('js-header-username');
    const headerRoles = document.getElementById('js-header-roles');
    const response = await fetch('/admin/myuser');
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