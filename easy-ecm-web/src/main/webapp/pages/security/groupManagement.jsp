<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>



	<div id="groupContentPane3x" dojoType="dijit.layout.ContentPane" tabindex="0" region="center" role="document" aria-live="assertive" aria-atomic="true" minSize="20">
			
			<div dojoType="dojo.data.ItemFileReadStore" jsId="mailStore" url="http://localhost:8080/easy-ecm-web/users2.json" urlPreventCache="true"></div>
				<table dojoType="dojox.grid.DataGrid"
					region="top" minSize="20" splitter="true"
					jsId="table"
					id="table"
					store="mailStore" 
					query="{ distinguishName: '*' }" 
					onRowClick="onMessageClick"
					style="height: 150px;">
					<thead>
						<tr>
						<th field="emailAddress" width="70%" >User </th>

						<th field="distinguishName" width="30%" >Group member</th>
						
						</tr>
					</thead>
				</table> <!-- end of listPane -->



	</div>