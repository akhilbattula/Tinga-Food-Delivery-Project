<?php 
session_start();

include "./config.php";
mysqli_set_charset($conn, 'utf8mb4');

$res = array();

if(isset($_POST['uid'])){

    $uid = mysqli_real_escape_string($conn,htmlspecialchars($_POST['uid']));
    
    $query = "SELECT * FROM `tbl_address` WHERE `uid`='$uid' ORDER BY `created_at` DESC";

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
                    'message'=>"There are no addresses in the database."
                    ));

        }else{
          
            while($row = mysqli_fetch_array($result))
            {
            
                $id = $row['id'];
                
                $bean  = array('id'=> $row['id'],
                                'title'=>$row['title'],
                                'address'=>$row['address'],
                                'location_lat'=>$row['location_lat'],
                                'location_long'=>$row['location_long'],
                                );
                          
               array_push($res,array("success"=>1,
                      array("data" => $bean)
                              ));
          
            }
          
        }
    }
}

echo json_encode(array('response' => $res)); 