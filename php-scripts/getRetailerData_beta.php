<?php
define('HOST','localhost');
define('USER','psVishalPs');
define('PASS','vendoe1232');
define('DB','vendoe');

	$con = mysqli_connect(HOST,USER,PASS,DB);

	$id = $_POST['id'];
	//$id = "166";

	$sql = "SELECT * FROM sellers JOIN seller_addresses ON sellers.id=seller_addresses.seller_id where sellers.id = $id";

	$res = mysqli_query($con,$sql);

	$result = array();

	while($row = mysqli_fetch_array($res)){
		array_push($result,
		array(
		'name'=>$row[1],
		'bname'=>$row[2],
		'email'=>$row[3],
		'description'=>$row[6],
		'image'=>$row[9],
		'sopen'=>$row[12],
		'sclose'=>$row[13],
		'cashless'=>$row[10],
		'delivery'=>$row[11],
		'brand'=>$row[14],
		'city'=>$row[24],
		'state'=>$row[25],
		'country'=>$row[26],
		'pin'=>$row[27],
		'baddr'=>$row[21],				
		));
	}

	echo json_encode(array("result"=>$result));

mysqli_close($con);

?>