<!DOCTYPE html>
<html lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="">
        <meta name="author" content="">
        <link href="css/bootstrap.min.css" type="text/css" rel="stylesheet">
        <link href="DataTables-1.9.4/media/css/jquery.dataTables.css" rel="stylesheet">
        <link href="css/style.css" type="text/css" rel="stylesheet">
        <title>Educademy Courses</title>
    </head>
    <body>
        <?php include('includes/header.php'); ?>
        <div class="container">
            <div class="panel panel-success">
                <div class="panel-heading">Courses</div>
                <div class="panel-body">
                    <table id="table" class='table table-striped table-bordered'>
                        <thead>
                            <tr>
                                <th></th>
                                <th>Course</th>
                                <th>Rating</th>
                                <th>Category</a></th>
                                <th>Start Date</th>
                                <th>Duration (weeks)</th>
                                <th>Professor</th>
                                <th>Site</th>
                            </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <?php include('includes/footer.php'); ?>
        <!-- JS placed at the end of the document so the page loads faster -->
        <script src="js/jquery-2.1.0.min.js"></script>
        <script src="js/jquery.validate.min.js"></script>
        <script src="js/jquery.raty.js"></script>
        <script src="DataTables-1.9.4/media/js/jquery.dataTables.min.js"></script>
        <script src="js/application.js"></script>
        <script src="js/bootstrap.min.js"></script>

        <?php if (isset($_SESSION['username'])): ?>
        <script>
            var username = "<?= $_SESSION['username'] ?>";

            $.get("get_user_ratings.php", { username: username }, function(result) {
                console.log(result);
            });
        </script>
        <?php endif; ?>
    </body>
</html>
