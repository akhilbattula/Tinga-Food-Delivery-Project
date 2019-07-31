<?php 
session_start();

include "../config.php";

$response = array();
if(isset($_POST['d_id'])){

  $d_id = mysqli_real_escape_string($conn,htmlspecialchars($_POST['d_id']));

  $query = "SELECT * FROM `tbl_delivery` WHERE `delivery_id`='$d_id'";
  $result = mysqli_query($conn, $query);
  if (!$result) {
      echo mysqli_error($conn);
      array_push($response,
        array('success'=>0,
                  'message'=>"Server issue. Please Try agin later.",
                  ));
  }else{                
      $count = mysqli_num_rows($result);
      if($count === 0){
          array_push($response,
            array('success'=>0,
                  'message'=>"There are no restaurants in the database.",
                  ));
      }else{
          $row = mysqli_fetch_array($result);
          $id = $row['delivery_id'];

          $bean  = array('delivery_id'=>$row['delivery_id'],
                  'name'=>$row['fname'].' '.$row['lname'],
                  'address'=>$row['address'],
                  'latitude'=>$row['location_lat'],
                  'longitude'=>$row['location_long'],
                  'phone_number'=>$row['phone_number'],
                  'email'=>$row['email'],
                  'rating'=>$row['rating'],
                  'status'=>$row['status'],
                  'image_code'=>$row['image_code']
                  );
                    
         array_push($response,
            array('success'=>1,
                array('data' => $bean)));

      }
  }
}
echo json_encode(array('response' => $response));
