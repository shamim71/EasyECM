<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>



	<div id="userContentPane3x" dojoType="dijit.layout.ContentPane" tabindex="0" region="center" role="document" aria-live="assertive" aria-atomic="true" minSize="20">
			<div dojoType="dojo.data.ItemFileReadStore" jsId="userDataStore" url="http://localhost:8080/easy-ecm-web/users.json" urlPreventCache="true"></div>
			
	<div style="margin-top: 10px;">
		<table dojoType="dojox.grid.DataGrid" 
				query="{ id: '*' }" 
				jsId="usersDataGrid"
				id="usersDataGrid"
				store="userDataStore"
				sortInfo="1"
				style="width: 100%; height: 200px;"
				>
			<thead>
				<tr>
					<th field="emailAddress" width="20%">Email Addres</th>
					<th field="displayName" width="20%">Display name</th>
					<th field="id" width="20%">identifier</th>
					<th field="distinguishedName" width="30%">Distinguished name</th>
				</tr>
			</thead>
		</table>

