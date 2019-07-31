<?php 
session_start();

include "../config.php";
mysqli_set_charset($conn, 'utf8mb4');

$res = array();

if(isset($_POST['username']) && isset($_POST['password'])){

    $username = mysqli_real_escape_string($conn,htmlspecialchars($_POST['username']));
    $password = mysqli_real_escape_string($conn,htmlspecialchars($_POST['password']));
    
    $query = "SELECT * FROM `tbl_delivery` WHERE `username`='$username' AND `password`='$password'";

    $result = mysqli_query($conn, $query);
    
    if (!$result) {
        array_push($res,
          array('success'=>0,
                'message'=>"Server issue. Please agin later."
                    ));
                    
    }else{  
     
        $count = mysqli_num_rows($result);
        
        if($count === 0){
            array_push($res,
              array('success'=>0,
                    'message'=>"Invalid Credentials."
                    ));

        }else{
            
            $row = mysqli_fetch_array($result);
            $id = $row['delivery_id'];
            $name = $row['fname']." ".$row['lname'];
            $bean  = array('delivery_id'=> $row['delivery_id'],
                          'name'=>$name,
                          'address'=>$row['address'],
                          'latitude'=>$row['current_lat'],
                          'longitude'=>$row['current_long'],
                          'image_code'=>$row['image_code'],
                          'phone_number'=>$row['phone_number'],
                          'email'=>$row['email'],
                          'rating'=>$row['rating'],
                          'status'=>$row['status']
                            );
                        
             array_push($res,array("success"=>1,
                    array("data" => $bean)
                    ));
            
        }
    }
}
echo json_encode(array('response' => $res));