var binder = zkbind.$('$root');
function hello(){
    binder.command('hello', {data: 'value'});
}

