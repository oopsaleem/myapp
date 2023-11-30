import { defineComponent, inject, onMounted, ref, type Ref } from 'vue';

import EmpService from './emp.service';
import { type IEmp } from '@/shared/model/emp.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Emp',
  setup() {
    const empService = inject('empService', () => new EmpService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const emps: Ref<IEmp[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveEmps = async () => {
      isFetching.value = true;
      try {
        const res = await empService().retrieve();
        emps.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveEmps();
    };

    onMounted(async () => {
      await retrieveEmps();
    });

    const removeId: Ref<string> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IEmp) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeEmp = async () => {
      try {
        await empService().delete(removeId.value);
        const message = 'A Emp is deleted with identifier ' + removeId.value;
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveEmps();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      emps,
      handleSyncList,
      isFetching,
      retrieveEmps,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeEmp,
    };
  },
});
